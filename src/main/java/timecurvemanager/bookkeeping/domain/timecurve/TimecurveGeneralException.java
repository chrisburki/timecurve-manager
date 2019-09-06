package timecurvemanager.bookkeeping.domain.timecurve;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TimecurveGeneralException extends RuntimeException {

  private TimecurveGeneralException(String text) {
    super("Error: " + text);
  }

  public static TimecurveGeneralException timecurveGeneralException(String text) {
    return new TimecurveGeneralException(text);
  }
}
