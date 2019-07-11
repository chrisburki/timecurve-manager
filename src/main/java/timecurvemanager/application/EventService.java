package timecurvemanager.application;

import static timecurvemanager.domain.event.EventClearingNotZeroException.eventClearingNotZero;
import static timecurvemanager.domain.event.EventNotFoundException.eventNotFound;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import java.util.HashMap;

import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;

import timecurvemanager.domain.event.EventRepository;
import timecurvemanager.domain.event.EventStatus;

@Service
@Slf4j
public class EventService {

  private static final String primaryKey = "Primary Key (Id)";
  private static final String extEventId = "Event Id (external)";

  public static final LocalDate minDate = LocalDate.of(1900,01,01);
  public static final LocalDate maxDate = LocalDate.of(4712,12,31);

  private final EventRepository eventRepository;
  private final EventItemRepository eventItemRepository;
  private final ApprovedBalanceService approvedBalanceService;

  public EventService(EventRepository eventRepository,
      EventItemRepository eventItemRepository,
      ApprovedBalanceService approvedBalanceService) {
    this.eventRepository = eventRepository;
    this.eventItemRepository = eventItemRepository;
    this.approvedBalanceService = approvedBalanceService;
  }

  /*
   * Helper - nvl
   */
  private <T> T nvl(T arg0, T arg1) {
    return (arg0 == null) ? arg1 : arg0;
  }

  /*
   * searchEvents
   * ************
   */

  /* Search for existing event(s) based on id - primary key - probably not needed*/
  public Event getEventById(Long id) {
    return eventRepository.findById(id).orElseThrow(() -> eventNotFound(id, primaryKey));
  }

  /* Search for latest version of event based on event id - external*/
  public Event getEventByEventExtId(Long eventId, Boolean inclItems) {
    int cntItems = 0;
    List<EventItem> eventItems = new ArrayList<>();
    if (inclItems) {
      eventItems = eventItemRepository.findQueryByEventExtId(eventId);
      cntItems = eventItems.size();
    }
    if (cntItems == 0) {
      return eventRepository.findQueryByEventExtId(eventId)
          .orElseThrow(() -> eventNotFound(eventId, extEventId));
    } else {
      Event event = eventItems.get(0).getEvent();
      eventItems.stream().forEach(eventItem -> {
        eventItem.setEvent(null);
        event.addEventItem2(eventItem);
      });
      return event;
    }
  }

  /* List events */
  public Collection<Event> listEvents(EventDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {

    LocalDate fDate1 = nvl(fromDate1, minDate);
    LocalDate tDate1 = nvl(toDate1, maxDate);
    LocalDate fDate2 = nvl(fromDate2, minDate);
    LocalDate tDate2 = nvl(toDate2, maxDate);

    return eventRepository.findQueryEvents(dimension, fDate1, tDate1, fDate2, tDate2, useCase);
  }

  /*
   * addEvent
   * ********
   */
  /* Check Clearing (part of addEvent) */
  private void buildClearing(HashMap<String, BigDecimal> clearingMap, String
      clearingReference,
      BigDecimal value) {
    clearingMap.put(clearingReference, clearingMap
        .getOrDefault(clearingReference, BigDecimal.ZERO).add(value));
  }

  private void evalClearing(Event event) {
    HashMap<String, BigDecimal> clearingMap = new HashMap<>();
    //1. Build Clearing Map
    event.getEventItems().stream()
        .filter(eventItem -> eventItem.getTimecurve().getClearingReference() != null)
        .forEach(eventItem -> {
          buildClearing(clearingMap, eventItem.getTimecurve().getClearingReference(),
              eventItem.getValue1());
        });
    // 2. Check Clearing Map for != 0
    clearingMap.entrySet().stream().filter(e -> e.getValue().compareTo(BigDecimal.ZERO) != 0)
        .forEach(f -> {
          throw eventClearingNotZero(f.getKey(), f.getValue());
        });
  }

  private EventItem reverseItem(EventItem item) {
    item.setIdToNull();
    item.setValue1(item.getValue1().negate());
    if (item.getValue2() != null) {
      item.setValue2(item.getValue2().negate());
    }
    if (item.getValue3() != null) {
      item.setValue3(item.getValue3().negate());
    }
    item.setTover1(item.getTover1().negate());
    if (item.getValue2() != null) {
      item.setTover2(item.getValue2().negate());
    }
    if (item.getValue3() != null) {
      item.setTover3(item.getValue3().negate());
    }
    return item;
  }

  private Event getLastEvent(Long evtExtId) {
    Event event = getEventByEventExtId(evtExtId, true);
    log.info("LAST EVENT: " +event.getEventExtId() + " : "+ event.getSequenceNr());
    event.setSequenceNr(-event.getSequenceNr());
    event.setIdToNull();
    event.getEventItems().forEach(eventItem -> reverseItem(eventItem));
    return event;
  }

  private Event addEventExtId(Event event, Long eventExtId, Integer sequenceNr) {
    event.setEventExtId(eventExtId);
    event.setSequenceNr(sequenceNr);
    return event;
  }

  private Event putEvent(Event event, Event lastEvent) {
    /*reveres last event if not null*/
    if (lastEvent != null) {
      eventRepository.save(lastEvent);
    }

    /*insert new one with higher sequenceNr*/
    return eventRepository.save(event);
  }

  private Event relvUpdate(Event event) {
    if (event != null && (event.getStatus().equals(EventStatus.APPROVED) || event
        .getStatus().equals(EventStatus.BOOKED))) {
      return event;
    }
    return null;
  }

  private void updateBalance(Event newEvent, Event lastEvent) {
    approvedBalanceService.addEvent(newEvent, lastEvent);
  }

  /* MAIN: Add Event. If existing reverse items and reinsert new ones.
     Additional check clearing consistency and update ApprovedBalance*/
  @Transactional
  public Event addEvent(Event event) {
    // 1. evaluate clearing
    evalClearing(event);

    // 2. find EventExtId and if not existing add one
    Event lastEvent = null;
    if (event.getEventExtId() == null) {
      event = addEventExtId(event, eventRepository.getNextEventExtId(), 1);
    } else {
      lastEvent = getLastEvent(event.getEventExtId());
      log.info("NEW EVENT: " +lastEvent.getEventExtId() + " : "+ (-lastEvent.getSequenceNr() + 1));
      event = addEventExtId(event, lastEvent.getEventExtId(), -lastEvent.getSequenceNr() + 1);
    }

    // 3. add event & items
    event = putEvent(event, lastEvent);

    // 4. update balance
    updateBalance(relvUpdate(event), relvUpdate(lastEvent));

    return event;
  }
}
