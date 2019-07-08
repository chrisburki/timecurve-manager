package timecurvemanager.domain.position;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class PositionAddException extends RuntimeException {

  private PositionAddException(Long id, String tag) {
    super("Timecurve Object object with id: " + id + " tag: " + tag + " already exists.");
  }

  public static PositionAddException positionAddException(Long id, String tag) {
    return new PositionAddException(id, tag);
  }
}
