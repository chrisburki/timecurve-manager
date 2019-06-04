package timecurvemanager.domain.event;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import timecurvemanager.domain.timecurveObject.TimecurveObject;

public interface EventItemRepository {

  List<EventItem> findAll();

  Optional<EventItem> findById(Long id);

  List<EventItem> findByEvent(Event event);

  List<EventItem> findByDimensionAndTimecurveAndItemTypeAndItemIdAndDate1Between(
      EventDimension dimension, TimecurveObject timecurve, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate);

  List<EventItem> findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate2Between(
      EventDimension dimension, TimecurveObject timecurve, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate);

  List<EventItem> findByDimensionAndTimecurveAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(
      EventDimension dimension, TimecurveObject timecurve, EventItemType itemType, Long itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2);

  EventItem save(EventItem eventItem);

}