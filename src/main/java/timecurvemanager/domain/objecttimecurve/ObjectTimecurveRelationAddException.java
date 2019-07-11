package timecurvemanager.domain.objecttimecurve;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class ObjectTimecurveRelationAddException extends RuntimeException {

  private ObjectTimecurveRelationAddException(Long objectId, Long timecurveId,
      LocalDate refDate) {
    super("Object Timecurve Realtion not found for ObjectId: " + objectId
        + " and reference date: " + refDate);
  }

  public static ObjectTimecurveRelationAddException objectTimecurveRelationAdd(
      Long objectId, Long timecurveId, LocalDate refDate) {
    return new ObjectTimecurveRelationAddException(objectId, timecurveId, refDate);
  }
}
