package timecurvemanager.domain.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException  {

  private EventNotFoundException(Long ident, String type) {
    super("Timecurve Object with " + type + ": " + ident.toString() + " does not exist");
  }

  public static EventNotFoundException eventNotFound(Long ident,
      String type) {
    return new EventNotFoundException(ident, type);
  }
}
