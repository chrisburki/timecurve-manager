package timecurvemanager.bookkeeping.domain.booking.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class BookingAddException extends RuntimeException {

  private BookingAddException(Long id, Long bookingExtId, Integer sequenceNr) {
    super("Booking with id: " + id + ", external Booking Id: " + bookingExtId + ", sequence Nr: "
        + sequenceNr + " already exists");
  }

  public static BookingAddException bookingAddException(Long id, Long bookingExtId,
      Integer sequenceNr) {
    return new BookingAddException(id, bookingExtId, sequenceNr);
  }
}
