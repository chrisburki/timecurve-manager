package timecurvemanager.position.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PositionNotFoundException extends RuntimeException {

  private PositionNotFoundException(String ident, String type) {
    super("Position with " + type + ": " + ident + " does not exist");
  }

  public static PositionNotFoundException positionNotFound(String ident,
      String type) {
    return new PositionNotFoundException(ident, type);
  }
}
