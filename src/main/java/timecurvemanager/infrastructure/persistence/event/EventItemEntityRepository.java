package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@Repository
public interface EventItemEntityRepository extends JpaRepository<EventItemEntity, Long> {

  @Query("select i "
      + "from  EventItemEntity i JOIN FETCH i.eventEntity e "
      + "where e.eventExtId = :eventExtId "
      + "  and e.sequenceNr = (select max(f.sequenceNr) from EventEntity f where f.eventExtId = :eventExtId)")
  List<EventItemEntity> findQueryByEventExtId(@Param("eventExtId") Long eventExtId);

  @Query("select i "
      + "from  EventItemEntity i JOIN FETCH i.eventEntity e "
      + "where e.dimension = :dimension"
      + "  and (:objectId is null or i.timecurveEntity.id = :objectId)"
      + "  and (:useCase  is null or e.useCase            = :useCase)"
      + "  and (:itemType is null or i.itemType           = :itemType)"
      + "  and (:itemId   is null or i.itemId             = :itemId)"
      + "  and i.date1    between :fromDate1 and :toDate1"
      + "  and i.date2    between :fromDate2 and :toDate2")
  List<EventItemEntity> findQueryEventItems(
      @Param("dimension") EventDimension dimension, @Param("objectId") Long objectId,
      @Param("itemType") EventItemType itemType, @Param("itemId") Long itemId,
      @Param("fromDate1") LocalDate fromDate1, @Param("toDate1") LocalDate toDate1,
      @Param("fromDate2") LocalDate fromDate2, @Param("toDate2") LocalDate toDate2,
      @Param("useCase") String useCase);

}
