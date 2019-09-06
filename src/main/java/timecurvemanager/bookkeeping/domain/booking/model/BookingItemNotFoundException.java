package timecurvemanager.bookkeeping.domain.booking.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingItemNotFoundException extends RuntimeException {

  private BookingItemNotFoundException(Long ident, String type) {
    super("Booking Item with " + type + ": " + ident.toString() + " does not exist");
  }

  public static BookingItemNotFoundException bookingItemNotFound(Long ident, String type) {
    return new BookingItemNotFoundException(ident, type);
  }
}
