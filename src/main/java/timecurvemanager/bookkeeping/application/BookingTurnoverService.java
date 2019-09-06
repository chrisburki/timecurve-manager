package timecurvemanager.bookkeeping.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.bookkeeping.domain.booking.BookingRepository;
import timecurvemanager.bookkeeping.domain.booking.api.BookingQueryTurnover;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;
import timecurvemanager.bookkeeping.domain.booking.model.BookingItem;

@Service
@Slf4j
public class BookingTurnoverService {

  private final BookingService bookingService;
  private final BookingRepository bookingRepository;

  public BookingTurnoverService(BookingService bookingService,
      BookingRepository bookingRepository) {
    this.bookingService = bookingService;
    this.bookingRepository = bookingRepository;
  }

  private void convertBookingItemToTurnover(HashMap<String, BookingQueryTurnover> turnoverMap,
      Booking booking,
      BookingItem item) {
    String key =
        booking.getTenantId() + ":" + booking.getDimension() + ":" + item.getTimecurveId() + ":"
            + item.getItemType() + ":" + item.getItemId();
    BookingQueryTurnover turnover = turnoverMap.get(key);
    if (turnover == null) {
      turnover = new BookingQueryTurnover(booking.getTenantId(), booking.getDimension(),
          item.getTimecurveId(),
          item.getItemType(), item.getItemId(), item.getValue1(),
          bookingService.nvl(item.getValue2(), BigDecimal.ZERO),
          bookingService.nvl(item.getValue3(), BigDecimal.ZERO), item.getGsn());
    } else {
      // update balance
      BigDecimal newTover1 = new BigDecimal(0)
          .add(turnover.getToverValue1().add(item.getValue1()));
      BigDecimal newTover2 = new BigDecimal(0)
          .add(
              turnover.getToverValue2().add(bookingService.nvl(item.getValue2(), BigDecimal.ZERO)));
      BigDecimal newTover3 = new BigDecimal(0)
          .add(
              turnover.getToverValue3().add(bookingService.nvl(item.getValue3(), BigDecimal.ZERO)));
      turnover.setToverValue1(newTover1);
      turnover.setToverValue2(newTover2);
      turnover.setToverValue3(newTover3);
      turnover.setMaxGsn(Math.max(item.getGsn(), turnover.getMaxGsn()));
    }
    turnoverMap.put(key, turnover);
  }

  public Collection<BookingQueryTurnover> getBookingTurnoverByGsnRange(Long timecurveId,
      BookKeepingDimension dimension,
      BookKeepingItemType itemType, Long itemId, LocalDate maxDate1, LocalDate maxDate2,
      Long fromGsn, Long toGsn) {
    LocalDate lMaxDate1 = bookingService.nvl(maxDate1, bookingService.maxDate);
    LocalDate lMaxDate2 = bookingService.nvl(maxDate2, bookingService.maxDate);

    HashMap<String, BookingQueryTurnover> turnoverMap = new HashMap<>();

    List<Booking> bookings = bookingRepository
        .findQueryByTimecurveIdAndGsnBetween(timecurveId, dimension, itemType, itemId, lMaxDate1,
            lMaxDate2, fromGsn, toGsn);
    bookings.forEach(booking -> {
      booking.getBookingItems().forEach(bookingItem -> {
        convertBookingItemToTurnover(turnoverMap, booking, bookingItem);
      });
    });
    return turnoverMap.values();
  }
}
