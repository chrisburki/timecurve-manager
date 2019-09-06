package timecurvemanager.bookkeeping.domain.timecurve;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class TimecurveAddException extends RuntimeException {

  private TimecurveAddException(Long id) {
    super("Timecurve Object object with id: " + id + " already exists.");
  }

  public static TimecurveAddException timecurveAddException(Long id) {
    return new TimecurveAddException(id);
  }
}
