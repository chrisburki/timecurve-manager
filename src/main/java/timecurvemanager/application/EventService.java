package timecurvemanager.application;

import static timecurvemanager.domain.event.EventClearingNotZeroException.eventClearingNotZero;
import static timecurvemanager.domain.event.EventNotFoundException.eventNotFound;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.Collection;

import java.util.HashMap;

import javax.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemType;

import timecurvemanager.domain.event.EventRepository;
import timecurvemanager.domain.event.EventStatus;

@Service
public class EventService {

  private static final String primaryKey = "Primary Key (Id)";
  private static final String extEventId = "Event Id (external)";

  private final EventRepository eventRepository;
  private final ApprovedBalanceService approvedBalanceService;

  public EventService(EventRepository eventRepository,
      ApprovedBalanceService approvedBalanceService) {
    this.eventRepository = eventRepository;
    this.approvedBalanceService = approvedBalanceService;
  }

  /*
   * searchEvents
   * ************
   */

  /* Search for existing event(s) based on id - primary key - probably not needed*/
  public Event getEventById(Long id) {
    return eventRepository.findById(id).orElseThrow(() -> eventNotFound(id, primaryKey));
  }

  /* Search for latest version of event(s) based on event id - external*/
  public Event getEventByEventId(Long eventId) {
    return eventRepository.findByEventExtId(eventId)
        .orElseThrow(() -> eventNotFound(eventId, extEventId));
  }

  /* List events */
  public Collection<Event> listEvents(EventDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String usecase) {
    Boolean data1NotNull = fromDate1 != null || toDate1 != null;
    Boolean data2NotNull = fromDate2 != null || toDate2 != null;
    ExampleMatcher matcher = ExampleMatcher.matching().withIncludeNullValues().withIgnoreCase();
    Example<String> useCaseMatch = Example.of(usecase, matcher);
    if (data1NotNull && data2NotNull) {
      return eventRepository
          .findByDimensionAndDate1BetweenAndDate2BetweenAndUseCase(dimension, fromDate1, toDate1,
              fromDate2, toDate2, useCaseMatch);
    } else if (data1NotNull) {
      return eventRepository
          .findByDimensionAndDate1BetweenAndUseCase(dimension, fromDate1, toDate1, useCaseMatch);
    } else {
      return eventRepository.findByDimensionAndDate2BetweenAndUseCase(dimension, fromDate2, toDate2,
          useCaseMatch);
    }
  }

  /* List event items */
  public Collection<EventItem> listEventItems(Long timecurveId, EventDimension dimension,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2,
      EventItemType itemType) {
    return null;
  }

  /*
   * addEvent
   * ********
   */

  private void putEvent(Event event) {
    /*get last event, event items (with highest sequenceNr*/
    /*if found reverese it and insert new one with higher sequenceNr*/
    //@todo: reverse if existing
    //@todo: reverse if existing
    eventRepository.save(event);
  }

  /* Check Clearing (part of addEvent) */
  private void buildClearing(HashMap<String, BigDecimal> clearingMap, String clearingReference,
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

  /* MAIN: Add Event. If existing reverse items and reinsert new ones. Additional check clearing consistency and update ApprovedBalance*/
  @Transactional
  public Event addEvent(Event event) {
    // 1. evaluate clearing
    evalClearing(event);
    // 2. add event & items
    putEvent(event);
    // 3. update balance
    //@todo: check for passing or reaching approved status
    if (event.getStatus() == EventStatus.APPROVED) {
      approvedBalanceService.addEvent(event);
    }
    return event;
  }


}
