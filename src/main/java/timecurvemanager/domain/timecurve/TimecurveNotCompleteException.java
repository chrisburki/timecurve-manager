package timecurvemanager.domain.timecurve;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimecurveNotCompleteException extends RuntimeException {

  private TimecurveNotCompleteException(Long id, String attribute) {
    super("Missing attribute " + attribute + " for timecurve object with id: " + id);
  }

  public static TimecurveNotCompleteException timecurveNotComplete(Long id,
      String attribute) {
    return new TimecurveNotCompleteException(id, attribute);
  }
}
