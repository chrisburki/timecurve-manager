package timecurvemanager.bookkeeping.infrastructure.persistence.timecurve;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimecurveEntityRepository extends
    JpaRepository<TimecurveEntity, Long> {

  List<TimecurveEntity> findByName(String label);

  @Query("select t from TimecurveEntity t JOIN FETCH t.timecurveRelations r "
      + "where r.objectId = :objectId "
      + "and   :refDate between r.validFrom and r.validTo")
  Optional<TimecurveEntity> findByObjectIdAndRefDate(
      @Param("objectId") String objectId, @Param("refDate") LocalDate refDate);
}
