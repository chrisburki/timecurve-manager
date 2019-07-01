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

  private static final LocalDate minDate = LocalDate.MIN;
  private static final LocalDate maxDate = LocalDate.MAX;

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

    LocalDate fDate1 = nvl(fromDate1, LocalDate.MAX);
    LocalDate tDate1 = nvl(toDate1, LocalDate.MIN);
    LocalDate fDate2 = nvl(fromDate2, LocalDate.MAX);
    LocalDate tDate2 = nvl(toDate2, LocalDate.MIN);

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

  private Event addEventExtId(Event event, Long eventExtId, Integer sequenceNr) {
    event.setEventExtId(eventExtId);
    event.setSequenceNr(sequenceNr);
    return event;
  }

  private Event putEvent(Event event, Event lastEvent) {
    /*reveres last event if not null*/
    if (lastEvent != null) {
      Event lastReversedEvent = new Event(
          null, lastEvent.getEventExtId(), -lastEvent.getSequenceNr(), lastEvent.getTenantId(),
          lastEvent.getDimension(), lastEvent.getStatus(), lastEvent.getUseCase(),
          lastEvent.getDate1(), lastEvent.getDate2());
      lastEvent.getEventItems().forEach(item -> {
        lastReversedEvent.addEventItem(item);
      });
      eventRepository.save(lastReversedEvent);
    }

    /*insert new one with higher sequenceNr*/
    return eventRepository.save(event);
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
      lastEvent = getEventByEventExtId(event.getEventExtId(), false);
      event = addEventExtId(event, lastEvent.getEventExtId(), lastEvent.getSequenceNr() + 1);
    }

    // 3. add event & items
    event = putEvent(event, lastEvent);

    // 4. update balance
    //@todo: check for passing or reaching approved status
    if (event.getStatus() == EventStatus.APPROVED) {
      approvedBalanceService.addEvent(event);
    }
    return event;
  }
}
