package timecurvemanager.domain.timecurveobject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TimecurveObjectNotFoundException extends RuntimeException {

  private TimecurveObjectNotFoundException(String ident, String type) {
    super("Timecurve Object with " + type + ": " + ident + " does not exist");
  }

  public static TimecurveObjectNotFoundException timecurveObjectNotFound(String ident,
      String type) {
    return new TimecurveObjectNotFoundException(ident, type);
  }

}
