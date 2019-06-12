package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntity;

@Repository
public interface EventItemEntityRepository extends JpaRepository<EventItemEntity, Long> {

  List<EventItemEntity> findByEventEntity(EventEntity eventEntity);

  List<EventItemEntity> findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate1Between(
      EventDimension dimension, TimecurveObjectEntity timecurveEntity, EventItemType itemType,
      Long itemId,
      LocalDate fromDate, LocalDate toDate);

  List<EventItemEntity> findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate2Between(
      EventDimension dimension, TimecurveObjectEntity timecurveEntity, EventItemType itemType,
      Long itemId,
      LocalDate fromDate, LocalDate toDate);

  List<EventItemEntity> findByDimensionAndTimecurveEntityAndItemTypeAndItemIdAndDate1BetweenAndDate2Between(
      EventDimension dimension, TimecurveObjectEntity timecurveEntity, EventItemType itemType,
      Long itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2);

}
