package timecurvemanager.application;

import org.springframework.transaction.annotation.Transactional;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemType;

import java.time.LocalDate;
import java.util.Collection;

public class EventService {

  /*
   * searchEvents
   * ************
   * Search for existing event(s) based on various parameter*/
  public Event getEventById(Long id) {
    return null;
  }

  public Event getEventByEventId(Long eventId) {
    return null;
  }

  public Collection<Event> listEvents(EventDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String usecase) {
    return null;
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
