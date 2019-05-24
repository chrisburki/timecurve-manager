package timecurvemanager.domain.event;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventItemRepository {

  List<EventItem> findAll();

  Optional<EventItem> findById(Long id);

  List<EventItem> findByEvent(Event event);

  List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate);

  List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate2Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate);

  List<EventItem> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2);

  EventItem save(EventItem eventItem);

}