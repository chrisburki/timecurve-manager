package timecurvemanager.bookkeeping.infrastructure.persistence.booking;

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
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;

@Repository
public interface BookingEntityRepository extends JpaRepository<BookingEntity, Long>,
    JpaSpecificationExecutor<BookingEntity> {

  @Query("select e from BookingEntity e "
      + "where e.bookingExtId = :bookingExtId "
      + "and e.sequenceNr = (select max(f.sequenceNr) from BookingEntity f where f.bookingExtId = :bookingExtId)")
  Optional<BookingEntity> findLastByBookingExtId(@Param("bookingExtId") Long bookingExtId);

  default List<BookingEntity> findByFilterCriteria(
      BookKeepingDimension dimension, LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2,
      LocalDate toDate2, String useCase, Long maxGsn
  ) {
    return findAll((root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<Predicate>();
      predicates.add(root.get("dimension").in(dimension));
      predicates.add(criteriaBuilder.between(root.get("date1"), fromDate1, toDate1));
      predicates.add(criteriaBuilder.between(root.get("date2"), fromDate2, toDate2));
      if (useCase != null) {
        predicates.add(criteriaBuilder.equal(root.get("useCase"), useCase));
      }
      if (maxGsn != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxGsn"), maxGsn));
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

  // item based
  @Query("select e "
      + "from  BookingEntity e LEFT JOIN FETCH e.bookingItems i "
      + "where e.bookingExtId = :bookingExtId "
      + "  and e.sequenceNr = (select max(f.sequenceNr) from BookingEntity f where f.bookingExtId = :bookingExtId)")
  Optional<BookingEntity> findQueryByBookingExtId(@Param("bookingExtId") Long bookingExtId);

  @Query("select e "
      + "from  BookingEntity e JOIN FETCH e.bookingItems i "
      + "where e.dimension = :dimension"
      + "  and (:maxGsn      is null or e.gsn        <= maxGsn)"
      + "  and (:timecurveId is null or i.timecurveId = :timecurveId)"
      + "  and (:useCase     is null or e.useCase     = :useCase)"
      + "  and (:itemType    is null or i.itemType    = :itemType)"
      + "  and (:itemId      is null or i.itemId      = :itemId)"
      + "  and i.date1       between :fromDate1 and :toDate1"
      + "  and i.date2       between :fromDate2 and :toDate2")
  List<BookingEntity> findQueryBookingItems(
      @Param("dimension") BookKeepingDimension dimension, @Param("timecurveId") Long timecurveId,
      @Param("itemType") BookKeepingItemType itemType, @Param("itemId") Long itemId,
      @Param("fromDate1") LocalDate fromDate1, @Param("toDate1") LocalDate toDate1,
      @Param("fromDate2") LocalDate fromDate2, @Param("toDate2") LocalDate toDate2,
      @Param("useCase") String useCase, @Param("maxGsn") Long maxGsn);

  @Query("select e "
      + "from  BookingEntity e LEFT JOIN FETCH e.bookingItems i "
      + "where i.timecurveId = :timecurveId"
      + "  and i.dimension   = :dimension"
      + "  and (:itemType    is null or i.itemType           = :itemType)"
      + "  and (:itemId      is null or i.itemId             = :itemId)"
      + "  and i.date1       <= :maxDate1"
      + "  and i.date2       <= :maxDate2"
      + "  and i.gsn         between :fromGsn and :toGsn")
  List<BookingEntity> findByTimecurveEntityAndGsnBetween(
      @Param("timecurveId") Long timecurveId, @Param("dimension") BookKeepingDimension dimension,
      @Param("itemType") BookKeepingItemType itemType, @Param("itemId") Long itemId,
      @Param("maxDate1") LocalDate maxDate1, @Param("maxDate2") LocalDate maxDate2,
      @Param("fromGsn") Long fromGsn, @Param("toGsn") Long toGsn);

  @Query("select max(i.gsn) "
      + "from   BookingItemEntity i "
      + "where i.timecurveId = :timecurveId"
      + "  and i.dimension   = :dimension"
      + "  and i.itemType    = :itemType"
      + "  and (:itemId is null or i.itemId = :itemId)")
  Long findQueryLastGsnByTimecurve(
      @Param("timecurveId") Long timecurveId, @Param("dimension") BookKeepingDimension dimension,
      @Param("itemType") BookKeepingItemType itemType, @Param("itemId") Long itemId);

}
