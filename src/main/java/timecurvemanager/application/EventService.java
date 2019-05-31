package timecurvemanager.application;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemType;

import java.time.LocalDate;
import java.util.Collection;

import static timecurvemanager.domain.event.EventNotFoundException.EventNotFound;
import timecurvemanager.domain.event.EventRepository;

public class EventService {

  private static final String primaryKey = "Primary Key (Id)";
  private static final String extEventId = "Event Id (external)";

  private final EventRepository eventRepository;

  public EventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  /*
   * searchEvents
   * ************
   * Search for existing event(s) based on id - primary key*/
  public Event getEventById(Long id) {
    return eventRepository.findById(id).orElseThrow(() -> EventNotFound(id, primaryKey));
  }

  /* Search for latest version of event(s) based on event id - external*/
  public Event getEventByEventId(Long eventId) {
    return eventRepository.findByEventExtId(eventId).orElseThrow(() -> EventNotFound(eventId, extEventId));
  }

  public Collection<Event> listEvents(EventDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String usecase) {
    Boolean data1NotNull = fromDate1 != null || toDate1 != null;
    Boolean data2NotNull = fromDate2 != null || toDate2 != null;
    ExampleMatcher matcher = ExampleMatcher.matching().withIncludeNullValues().withIgnoreCase();
    Example<String> useCaseMatch = Example.of(usecase,matcher);
    if (data1NotNull && data2NotNull) {
      return eventRepository.findByDimensionAndDate1BetweenAndDate2BetweenAndUseCase(dimension,fromDate1, toDate1, fromDate2, toDate2, useCaseMatch);
    } else if (data1NotNull) {
      return eventRepository.findByDimensionAndDate1BetweenAndUseCase(dimension, fromDate1, toDate1, useCaseMatch);
    } else {
      return eventRepository.findByDimensionAndDate2BetweenAndUseCase(dimension,fromDate2,toDate2,
          useCaseMatch);
    }
  }

  public Collection<EventItem> listEventItems(Long timecurveId, EventDimension dimension,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2,
      EventItemType itemType) {
    return null;
  }

  /*
   * addEvent
   * ********
   * Search for existing event. If existing reverse items and reinsert new ones. Additional check clearing consistency*/
  @Transactional
  public Event addEvent(Event event) {
    return null;
  }

  public void checkClearing(Event event) {
  }

  ;

}
