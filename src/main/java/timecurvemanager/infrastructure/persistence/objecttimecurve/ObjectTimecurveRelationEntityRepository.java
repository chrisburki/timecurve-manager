package timecurvemanager.infrastructure.persistence.objecttimecurve;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectTimecurveRelationEntityRepository extends
    JpaRepository<ObjectTimecurveRelationEntity, Long> {

  @Query("select pt from ObjectTimecurveRelationEntity pt "
      + "where pt.objectId = :objectId "
      + "and   :refDate between pt.validFrom and pt.validTo")
  Optional<ObjectTimecurveRelationEntity> findByObjectRefDate(
      @Param("objectId") Long objectId, @Param("refDate") LocalDate refDate);
}
