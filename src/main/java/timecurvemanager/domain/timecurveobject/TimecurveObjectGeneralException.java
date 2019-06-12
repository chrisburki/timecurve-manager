package timecurvemanager.domain.timecurveobject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TimecurveObjectGeneralException extends RuntimeException {

  private TimecurveObjectGeneralException(String text) {
    super("Error: " + text);
  }

  public static TimecurveObjectGeneralException timecurveObjectGeneralException(String text) {
    return new TimecurveObjectGeneralException(text);
  }
}
