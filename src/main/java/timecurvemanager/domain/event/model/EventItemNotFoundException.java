package timecurvemanager.domain.event.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventItemNotFoundException extends RuntimeException {

  private EventItemNotFoundException(Long ident, String type) {
    super("Event Item with " + type + ": " + ident.toString() + " does not exist");
  }

  public static EventItemNotFoundException eventItemNotFound(Long ident, String type) {
    return new EventItemNotFoundException(ident, type);
  }
}
