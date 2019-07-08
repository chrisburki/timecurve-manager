package timecurvemanager.domain.positiontimecurve;

import java.time.LocalDate;
import java.util.Optional;

public interface PositionTimecurveRelationRepository {

  Optional<PositionTimecurveRelation> findByPositionRefDate(Long positionId, LocalDate refDate);

  PositionTimecurveRelation save(PositionTimecurveRelation positionTimecurveRelation);

  void delete(PositionTimecurveRelation relation);
}
