package timecurvemanager.domain.objecttimecurve;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class ObjectTimecurveRelationAddException extends RuntimeException {

  private ObjectTimecurveRelationAddException(String objectId, Long timecurveId,
      LocalDate refDate) {
    super("Object Timecurve Relation creation failed for ObjectId: " + objectId + " timecurveId: "
        + timecurveId + " and reference date: " + refDate);
  }

  public static ObjectTimecurveRelationAddException objectTimecurveRelationAdd(
      String objectId, Long timecurveId, LocalDate refDate) {
    return new ObjectTimecurveRelationAddException(objectId, timecurveId, refDate);
  }
}
