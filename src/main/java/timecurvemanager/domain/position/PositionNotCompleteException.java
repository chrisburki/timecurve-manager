package timecurvemanager.domain.position;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PositionNotCompleteException extends RuntimeException {

  private PositionNotCompleteException(Long id, String attribute) {
    super("Missing attribute " + attribute + " for position with id: " + id);
  }

  public static PositionNotCompleteException positionNotComplete(Long id,
      String attribute) {
    return new PositionNotCompleteException(id, attribute);
  }
}
