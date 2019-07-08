package timecurvemanager.domain.timecurveobject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TimecurveObjectNotFoundException extends RuntimeException {

  private TimecurveObjectNotFoundException(String ident) {
    super("Timecurve Object with id " + ident + " does not exist");
  }

  public static TimecurveObjectNotFoundException timecurveObjectNotFound(String ident) {
    return new TimecurveObjectNotFoundException(ident);
  }

}
