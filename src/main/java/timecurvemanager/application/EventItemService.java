package timecurvemanager.application;

import static timecurvemanager.domain.event.EventItemNotFoundException.EventItemNotFound;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;
import timecurvemanager.domain.event.EventItemType;
import timecurvemanager.domain.event.EventRepository;
import timecurvemanager.domain.timecurveobject.TimecurveObject;

@Service
public class EventItemService {

  private static final String primaryKey = "Primary Key (Id)";

  private final EventItemRepository itemRepository;
  private final EventService eventService;

  public EventItemService(
      EventItemRepository itemRepository, EventService eventService) {
    this.itemRepository = itemRepository;
    this.eventService = eventService;
  }

  /*
   * searchEvents
   * ************
   */

  /* Search for existing event(s) based on id - primary key - probably not needed*/
  public EventItem getEventItemById(Long id) {
    return itemRepository.findById(id).orElseThrow(() -> EventItemNotFound(id, primaryKey));
  }

  /* Search for latest version of event(s) based on event id - external*/
  public Collection<EventItem> getEventItemsByEventId(Long eventId) {
    return itemRepository.findByEvent(eventService.getEventById(eventId));
  }

  /* Search for latest version of event(s) based on event id - external*/
  public Collection<EventItem> getEventItemsByEventExtId(Long extEventId) {
    return itemRepository.findByEvent(eventService.getEventByEventExtId(extEventId));
  }

  /* List events items */
  public Collection<EventItem> listEventItems(EventDimension dimension, TimecurveObject timecurve,
      EventItemType itemType, Long itemId, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2) {
    Boolean data1NotNull = fromDate1 != null || toDate1 != null;
    Boolean data2NotNull = fromDate2 != null || toDate2 != null;
    //@todo: add validations

    // to ingore case and null values
    ExampleMatcher matcher = ExampleMatcher.matching().withIncludeNullValues();
    Example<Long> itemIdMatch = Example.of(itemId, matcher);

    if (data1NotNull && data2NotNull) {
      return itemRepository
          .findByDimensionAndTimecurveAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(dimension,
              timecurve, itemType, itemIdMatch, fromDate1, toDate1, fromDate2, toDate2);
    } else if (data1NotNull) {
      return itemRepository
          .findByDimensionAndTimecurveAndItemTypeAndItemIdAndDate1Between(dimension, timecurve,
              itemType, itemIdMatch, fromDate1, toDate1);
    } else {
      return itemRepository
          .findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate2Between(dimension,
              timecurve, itemType, itemIdMatch, fromDate2, toDate2);
    }
  }
}
