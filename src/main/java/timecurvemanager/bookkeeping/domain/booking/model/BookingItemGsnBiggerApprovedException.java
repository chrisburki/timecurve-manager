package timecurvemanager.bookkeeping.domain.booking.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SEE_OTHER)
public class BookingItemGsnBiggerApprovedException extends RuntimeException {

  private BookingItemGsnBiggerApprovedException(Long lastGsn, Long approvedGsn) {
    super("Last Gsn: " + lastGsn + " is bigger then apprved Gsn: " + approvedGsn);
  }

  public static BookingItemGsnBiggerApprovedException bookingItemGsnBiggerApproved(Long lastGsn,
      Long approvedGsn) {
    return new BookingItemGsnBiggerApprovedException(lastGsn, approvedGsn);
  }
}
