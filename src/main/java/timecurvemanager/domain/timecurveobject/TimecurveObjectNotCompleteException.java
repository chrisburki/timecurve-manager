package timecurvemanager.domain.timecurveobject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimecurveObjectNotCompleteException extends RuntimeException {

  private TimecurveObjectNotCompleteException(Long id, String attribute) {
    super("Missing attribute " + attribute + " for timecurve object with id: " + id);
  }

  public static TimecurveObjectNotCompleteException timecurveObjectNotComplete(Long id,
      String attribute) {
    return new TimecurveObjectNotCompleteException(id, attribute);
  }
}
