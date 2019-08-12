package timecurvemanager.application;

import java.time.LocalDate;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.BookKeepingItemType;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;

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
   * searchEvents
   * ************
   */

  /* Search for latest version of event(s) based on event id - external*/
  public Collection<EventItem> getEventItemsByEventExtId(Long eventId) {
    return itemRepository.findQueryByEventExtId(eventId);
  }

  /* List events items */
  public Collection<EventItem> listEventItems(BookKeepingDimension dimension, Long timecurveId,
      BookKeepingItemType itemType, Long itemId, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {
    Boolean data1NotNull = fromDate1 != null || toDate1 != null;
    Boolean data2NotNull = fromDate2 != null || toDate2 != null;
    //@todo: add validations

    LocalDate fDate1 = eventService.nvl(fromDate1, eventService.minDate);
    LocalDate tDate1 = eventService.nvl(toDate1, eventService.maxDate);
    LocalDate fDate2 = eventService.nvl(fromDate2, eventService.minDate);
    LocalDate tDate2 = eventService.nvl(toDate2, eventService.maxDate);

    return itemRepository
        .findQueryEventItems(dimension, timecurveId, itemType, itemId, fDate1, tDate1, fDate2,
            tDate2,
            useCase);
  }

  public Collection<EventItem> getEventItemByGsn(Long timecurveId, BookKeepingDimension dimension,
      BookKeepingItemType itemType, Long itemId, LocalDate maxDate1, LocalDate maxDate2, Long fromGsn, Long toGsn) {
    LocalDate lMaxDate1 = eventService.nvl(maxDate1, eventService.maxDate);
    LocalDate lMaxDate2 = eventService.nvl(maxDate2, eventService.maxDate);

    return itemRepository
        .findQueryByTimecurveIdAndGsnBetween(timecurveId, dimension, itemType, itemId, lMaxDate1, lMaxDate2, fromGsn, toGsn);
  }

  public Long getLastGsnByTimecurve(Long timecurveId, BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId) {
    return itemRepository.findQueryLastGsnByTimecurve(timecurveId, dimension, itemType, itemId);
  }
}
