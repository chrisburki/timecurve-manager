package timecurvemanager.bookkeeping.domain.objecttimecurve;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ObjectTimecurveRelationRepository {

  List<ObjectTimecurveRelation> findByObjectIdOrderByValidFromAsc(String objectId);

  Optional<ObjectTimecurveRelation> findByTimecurveIdAndRefDate(Long timecurveId,
      LocalDate refDate);

  Optional<ObjectTimecurveRelation> findByObjectIdAndRefDate(String objectId, LocalDate refDate);

//  ObjectTimecurveRelation save(ObjectTimecurveRelation objectTimecurveRelation);

  void delete(ObjectTimecurveRelation relation);
}
