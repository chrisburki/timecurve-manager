package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timecurvemanager.domain.event.model.BookKeepingDimension;

@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, Long>,
    JpaSpecificationExecutor<EventEntity> {

  @Query("select e from EventEntity e "
      + "where e.eventExtId = :eventExtId "
      + "and e.sequenceNr = (select max(f.sequenceNr) from EventEntity f where f.eventExtId = :eventExtId)")
  Optional<EventEntity> findLastByEventExtId(@Param("eventExtId") Long eventExtId);

  default List<EventEntity> findByFilterCriteria(
      BookKeepingDimension dimension, LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2,
      LocalDate toDate2, String useCase
  ) {
    return findAll((root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<Predicate>();
      predicates.add(root.get("dimension").in(dimension));
      predicates.add(criteriaBuilder.between(root.get("date1"), fromDate1, toDate1));
      predicates.add(criteriaBuilder.between(root.get("date2"), fromDate2, toDate2));
      if (useCase != null) {
        predicates.add(criteriaBuilder.equal(root.get("useCase"), useCase));
      }
//      if (internalKey != null) {
//        predicates.add(criteriaBuilder.like(root.get("internalKey"), "%" + internalKey + "%"));
//      }
//      if (currencyIsoKeyList != null && !currencyIsoKeyList.isEmpty()) {
//        predicates.add(root.get("currencyIsoKey").in(currencyIsoKeyList));
//      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    });
  }
}
