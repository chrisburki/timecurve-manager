package timecurvemanager.domain.timecurveobject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class TimecurveObjectAddException extends RuntimeException {

  private TimecurveObjectAddException(Long id, String tag) {
    super("Timecurve Object object with id: " + id + " tag: " + tag + " already exists.");
  }

  public static TimecurveObjectAddException timecurveObjectAddException(Long id, String tag) {
    return new TimecurveObjectAddException(id, tag);
  }
}
