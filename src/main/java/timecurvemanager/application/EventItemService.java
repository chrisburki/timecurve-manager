package timecurvemanager.application;

import java.time.LocalDate;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;
import timecurvemanager.domain.event.EventItemType;

@Service
@Slf4j
public class EventItemService {

  private final EventItemRepository itemRepository;
  private final EventService eventService;

  public EventItemService(
      EventItemRepository itemRepository, EventService eventService) {
    this.itemRepository = itemRepository;
    this.eventService = eventService;
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

  /* Search for latest version of event(s) based on event id - external*/
  public Collection<EventItem> getEventItemsByEventExtId(Long eventId) {
    return itemRepository.findQueryByEventExtId(eventId);
  }

  /* List events items */
  public Collection<EventItem> listEventItems(EventDimension dimension, Long objectId,
      EventItemType itemType, Long itemId, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {
    Boolean data1NotNull = fromDate1 != null || toDate1 != null;
    Boolean data2NotNull = fromDate2 != null || toDate2 != null;
    //@todo: add validations

    LocalDate fDate1 = nvl(fromDate1, eventService.minDate);
    LocalDate tDate1 = nvl(toDate1, eventService.maxDate);
    LocalDate fDate2 = nvl(fromDate2, eventService.minDate);
    LocalDate tDate2 = nvl(toDate2, eventService.maxDate);

    return itemRepository
        .findQueryEventItems(dimension, objectId, itemType, itemId, fDate1, tDate1, fDate2, tDate2,
            useCase);
  }
}
