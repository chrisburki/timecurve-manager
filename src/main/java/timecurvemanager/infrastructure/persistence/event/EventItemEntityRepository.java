package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.BookKeepingItemType;

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
      + "  and (:timecurveId is null or i.timecurveId = :timecurveId)"
      + "  and (:useCase     is null or e.useCase     = :useCase)"
      + "  and (:itemType    is null or i.itemType    = :itemType)"
      + "  and (:itemId      is null or i.itemId      = :itemId)"
      + "  and i.date1       between :fromDate1 and :toDate1"
      + "  and i.date2       between :fromDate2 and :toDate2")
  List<EventItemEntity> findQueryEventItems(
      @Param("dimension") BookKeepingDimension dimension, @Param("timecurveId") Long timecurveId,
      @Param("itemType") BookKeepingItemType itemType, @Param("itemId") Long itemId,
      @Param("fromDate1") LocalDate fromDate1, @Param("toDate1") LocalDate toDate1,
      @Param("fromDate2") LocalDate fromDate2, @Param("toDate2") LocalDate toDate2,
      @Param("useCase") String useCase);

  @Query("select i "
      + "from  EventItemEntity i "
      + "where i.timecurveId = :timecurveId"
      + "  and i.dimension   = :dimension"
      + "  and (:itemType    is null or i.itemType           = :itemType)"
      + "  and (:itemId      is null or i.itemId             = :itemId)"
      + "  and i.date1       <= :maxDate1"
      + "  and i.date2       <= :maxDate2"
      + "  and i.gsn         between :fromGsn and :toGsn")
  List<EventItemEntity> findByTimecurveEntityAndGsnBetween(
      @Param("timecurveId") Long timecurveId, @Param("dimension") BookKeepingDimension dimension,
      @Param("itemType") BookKeepingItemType itemType, @Param("itemId") Long itemId,
      @Param("maxDate1") LocalDate maxDate1, @Param("maxDate2") LocalDate maxDate2,
      @Param("fromGsn") Long fromGsn, @Param("toGsn") Long toGsn);

  @Query("select max(i.gsn) "
      + "from   EventItemEntity i "
      + "where i.timecurveId = :timecurveId"
      + "  and i.dimension   = :dimension"
      + "  and i.itemType    = :itemType"
      + "  and (:itemId is null or i.itemId = :itemId)")
  Long findQueryLastGsnByTimecurve(
      @Param("timecurveId") Long timecurveId, @Param("dimension") BookKeepingDimension dimension,
      @Param("itemType") BookKeepingItemType itemType, @Param("itemId") Long itemId);

  Optional<EventItemEntity> findFirstByTimecurveIdAndDimensionAndItemTypeOrderByGsnDescEventEntityIdDesc(
      @Param("timecurveId") Long timecurveId, @Param("dimension") BookKeepingDimension dimension,
      @Param("itemType") BookKeepingItemType itemType);

}

