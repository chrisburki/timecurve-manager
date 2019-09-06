package timecurvemanager.bookkeeping.domain.booking.model;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class BookingClearingNotZeroException extends RuntimeException {

  private BookingClearingNotZeroException(String clearingReference, BigDecimal value) {
    super("Clearing for reference: " + clearingReference + " not zero: " + value);
  }

  public static BookingClearingNotZeroException bookingClearingNotZero(String clearingReference,
      BigDecimal value) {
    return new BookingClearingNotZeroException(clearingReference, value);
  }
}
