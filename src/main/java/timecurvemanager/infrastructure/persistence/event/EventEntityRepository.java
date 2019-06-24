package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timecurvemanager.domain.event.EventDimension;

@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, Long> {

  @Query("select e from EventEntity e "
      + "where e.eventExtId = :eventExtId "
      + "and e.sequenceNr = (select max(f.sequenceNr) from EventEntity f where f.eventExtId = :eventExtId)")
  Optional<EventEntity> findByEventExtId(@Param("eventExtId") Long eventExtId);

  @Query("select e "
      + "from EventEntity e "
      + ",EventItemEntity i "
      + "where e.eventExtId = :eventExtId "
      + "and e.sequenceNr = (select max(f.sequenceNr) from EventEntity f where f.eventExtId = :eventExtId)"
      + "and i.eventEntity = e.id")
  Optional<EventEntity> findEventItemByEventExtId(@Param("eventExtId") Long eventExtId);

  @Query("select e "
      + "from EventEntity e "
      + "where e.dimension = :dimension"
      + "  and (:useCase is null or e.useCase = :useCase)"
      + "  and e.date1 between :fromDate1 and :toDate1"
      + "  and e.date2 between :fromDate2 and :toDate2"
  )
  List<EventEntity> findQueryEvents(
      @Param("dimension") EventDimension dimension, @Param("fromDate1") LocalDate fromDate1,
      @Param("toDate1") LocalDate toDate1, @Param("fromDate2") LocalDate fromDate2,
      @Param("toDate2") LocalDate toDate2, @Param("useCase") String useCase);

  @Query("select e "
      + "from EventEntity e "
      + ",EventItemEntity i "
      + "where e.dimension = :dimension"
      + "  and (:useCase is null or e.useCase = :useCase)"
      + "  and e.date1 between :fromDate1 and :toDate1"
      + "  and e.date2 between :fromDate2 and :toDate2"
      + "  and i.eventEntity = e.id"
  )
  List<EventEntity> findEventItemsQueryEvents(
      @Param("dimension") EventDimension dimension, @Param("fromDate1") LocalDate fromDate1,
      @Param("toDate1") LocalDate toDate1, @Param("fromDate2") LocalDate fromDate2,
      @Param("toDate2") LocalDate toDate2, @Param("useCase") String useCase);
}
