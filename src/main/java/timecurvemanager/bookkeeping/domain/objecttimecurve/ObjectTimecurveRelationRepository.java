package timecurvemanager.bookkeeping.domain.objecttimecurve;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ObjectTimecurveRelationRepository {

  List<ObjectTimecurveRelation> findByObjectId(String objectId);

  Optional<ObjectTimecurveRelation> findByTimecurveAndRefDate(Long timecurveId, LocalDate refDate);

  Optional<ObjectTimecurveRelation> findByObjectRefDate(String objectId, LocalDate refDate);

  ObjectTimecurveRelation save(ObjectTimecurveRelation objectTimecurveRelation);

  void delete(ObjectTimecurveRelation relation);
}
