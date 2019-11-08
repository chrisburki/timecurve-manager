package timecurvemanager.bookkeeping.domain.booking;

import java.time.LocalDate;
import timecurvemanager.bookkeeping.domain.booking.model.BookingTimecurveDetail;

public interface BookingSpi {

  BookingTimecurveDetail addTimecurve(String objectId, LocalDate refDate);

  BookingTimecurveDetail getTimecurve(Long id, LocalDate refDate);
}
