package timecurvemanager.domain.objecttimecurve;

import java.time.LocalDate;
import java.util.Optional;

public interface ObjectTimecurveRelationRepository {

  Optional<ObjectTimecurveRelation> findByObjectRefDate(Long objectId, LocalDate refDate);

  ObjectTimecurveRelation save(ObjectTimecurveRelation objectTimecurveRelation);

  void delete(ObjectTimecurveRelation relation);
}
