package timecurvemanager.gsn.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GsnNotFoundException extends RuntimeException {

  private GsnNotFoundException(Long id) {
    super("GSN not found for id: " + id);
  }

  public static GsnNotFoundException gsnNotFound(Long id) {
    return new GsnNotFoundException(id);
  }
}
