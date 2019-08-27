package timecurvemanager.domain.event.model;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class EventClearingNotZeroException extends RuntimeException {

  private EventClearingNotZeroException(String clearingReference, BigDecimal value) {
    super("Clearing for reference: " + clearingReference + " not zero: " + value);
  }

  public static EventClearingNotZeroException eventClearingNotZero(String clearingReference,
      BigDecimal value) {
    return new EventClearingNotZeroException(clearingReference, value);
  }
}
