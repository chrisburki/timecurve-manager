package timecurvemanager.domain.objecttimecurve;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectTimecurveRelationNotFoundException extends RuntimeException {

  private ObjectTimecurveRelationNotFoundException(String objectId, LocalDate refDate) {
    super("Object Timecurve Realtion not found for ObjectId: " + objectId
        + " and reference date: " + refDate);
  }

  public static ObjectTimecurveRelationNotFoundException objectTimecurveRelationNotFound(
      String objectId, LocalDate refDate) {
    return new ObjectTimecurveRelationNotFoundException(objectId, refDate);
  }

}
