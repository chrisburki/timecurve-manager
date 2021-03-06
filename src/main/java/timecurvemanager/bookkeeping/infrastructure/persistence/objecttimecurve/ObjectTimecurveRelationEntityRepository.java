package timecurvemanager.bookkeeping.infrastructure.persistence.objecttimecurve;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectTimecurveRelationEntityRepository extends
    JpaRepository<ObjectTimecurveRelationEntity, Long> {

  List<ObjectTimecurveRelationEntity> findByObjectIdOrderByValidFromAsc(String objectId);

  @Query("select pt from ObjectTimecurveRelationEntity pt "
      + "where pt.objectId = :objectId "
      + "and   :refDate between pt.validFrom and pt.validTo")
  Optional<ObjectTimecurveRelationEntity> findByObjectIdAndRefDate(
      @Param("objectId") String objectId, @Param("refDate") LocalDate refDate);

  @Query("select pt from ObjectTimecurveRelationEntity pt "
      + "where pt.timecurveEntity.id = :timecurveId "
      + "and   :refDate between pt.validFrom and pt.validTo")
  Optional<ObjectTimecurveRelationEntity> findByTimecurveIdAndRefDate(
      @Param("timecurveId") Long timecuveId, @Param("refDate") LocalDate refDate);

}
