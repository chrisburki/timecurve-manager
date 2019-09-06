package timecurvemanager.bookkeeping.domain.booking.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException {

  private BookingNotFoundException(Long ident, String type) {
    super("Booking with " + type + ": " + ident.toString() + " does not exist");
  }

  public static BookingNotFoundException bookingNotFound(Long ident,
      String type) {
    return new BookingNotFoundException(ident, type);
  }
}
