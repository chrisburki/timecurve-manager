package timecurvemanager.infrastructure.persistence.event;

import org.springframework.data.jpa.repository.JpaRepository;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

import java.time.LocalDate;
import java.util.List;

public interface EventItemEntityRepository extends JpaRepository<EventItemEntity, Long> {

  List<EventItemEntity> findByEventEntity(EventEntity eventEntity);

  List<EventItemEntity> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate);

  List<EventItemEntity> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate2Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate, LocalDate toDate);

  List<EventItemEntity> findByDimensionAndTimecurveIdAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(
      EventDimension dimension, Long timecurveId, EventItemType itemType, Long itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2);

}
