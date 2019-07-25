package timecurvemanager.domain.timecurve;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TimecurveNotFoundException extends RuntimeException {

  private TimecurveNotFoundException(String ident) {
    super("Timecurve Object with id " + ident + " does not exist");
  }

  public static TimecurveNotFoundException timecurveNotFound(String ident) {
    return new TimecurveNotFoundException(ident);
  }

}
