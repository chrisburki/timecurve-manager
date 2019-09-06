package timecurvemanager.bookkeeping.domain.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;

public interface BookingRepository {

  Optional<Booking> findById(Long id);

  Optional<Booking> findLastByBookingExtId(Long bookingExtId);

  List<Booking> findQueryBookings(
      BookKeepingDimension dimension, LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2,
      LocalDate toDate2, String useCase, Long maxGsn);

  Booking save(Booking booking);

  /*internal*/
  Long getNextBookingExtId();

  // incl items
  Optional<Booking> findQueryByBookingExtId(Long bookingExtId);

  List<Booking> findQueryBookingItems(
      BookKeepingDimension dimension, Long timecurveId, BookKeepingItemType itemType, Long itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2,
      String useCase, Long maxGsn);

  List<Booking> findQueryByTimecurveIdAndGsnBetween(
      Long timecurveId, BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId,
      LocalDate maxDate1, LocalDate maxDate2, Long fromGsn, Long toGsn);

  Long findQueryLastGsnByTimecurve(
      Long timecurveId, BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId);

}
