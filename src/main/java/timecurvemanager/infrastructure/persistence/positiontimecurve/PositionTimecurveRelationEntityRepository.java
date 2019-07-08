package timecurvemanager.infrastructure.persistence.positiontimecurve;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionTimecurveRelationEntityRepository extends
    JpaRepository<PositionTimecurveRelationEntity, Long> {

  @Query("select pt from PositionTimecurveRelationEntity pt "
      + "where pt.positionEntity.id = :positionId "
      + "and   :refDate between pt.validFrom and pt.validTo")
  Optional<PositionTimecurveRelationEntity> findByPositionRefDate(Long positionId, LocalDate refDate);
}
