package timecurvemanager.bookkeeping.infrastructure.rest.booking;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import timecurvemanager.bookkeeping.application.BookingService;
import timecurvemanager.bookkeeping.application.BookingTurnoverService;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingQueryTurnover;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;

//@todo: replace get and post with messages

@RestController
@RequestMapping("/book-keeping")
public class BookingController {

  private final BookingService bookingService;
  private final BookingTurnoverService bookingTurnoverService;

  public BookingController(BookingService bookingService,
      BookingTurnoverService bookingTurnoverService) {
    this.bookingService = bookingService;
    this.bookingTurnoverService = bookingTurnoverService;
  }

  @GetMapping("/bookings")
  ResponseEntity<List<Booking>> listBookings(
      @RequestParam("dimension") BookKeepingDimension dimension,
      @RequestParam(value = "from-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate1,
      @RequestParam(value = "to-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate1,
      @RequestParam(value = "from-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate2,
      @RequestParam(value = "to-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate2,
      @RequestParam(value = "use-case", required = false) String useCase,
      @RequestParam(value = "max-gsn", required = false) Long maxGsn
  ) {
    return new ResponseEntity<>(
        new ArrayList<>(
            bookingService
                .listBookings(dimension, fromDate1, toDate1, fromDate2, toDate2, useCase, maxGsn)),
        HttpStatus.OK);
  }

  @PostMapping("/bookings")
  ResponseEntity<BookingExternalEvent> addBooking(@RequestBody BookingCommand booking,
      @RequestParam(value = "approved-gsn", required = false) Long approvedGsn) {
    BookingExternalEvent result = bookingService.processBookingCommand(booking);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/booking-items")
  ResponseEntity<List<Booking>> listBookingsByTimecurve(
      @RequestParam("dimension") BookKeepingDimension dimension,
      @RequestParam(value = "timecurve-id", required = false) Long timecurveId,
      @RequestParam(value = "item-type", required = false) BookKeepingItemType itemType,
      @RequestParam(value = "item-id", required = false) Long itemId,
      @RequestParam(value = "from-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate1,
      @RequestParam(value = "to-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate1,
      @RequestParam(value = "from-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate2,
      @RequestParam(value = "to-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate2,
      @RequestParam(value = "use-case", required = false) String useCase,
      @RequestParam(value = "max-gsn", required = false) Long maxGsn
  ) {
    return new ResponseEntity<>(
        new ArrayList<>(
            bookingService
                .listBookingsForTimecurve(dimension, timecurveId, itemType, itemId, fromDate1,
                    toDate1,
                    fromDate2, toDate2, useCase, maxGsn)),
        HttpStatus.OK);
  }

  @GetMapping("/bookings/{id}")
  ResponseEntity<Booking> getBookingByExtId(
      @PathVariable("id") Long bookingExtId,
      @RequestParam(value = "include-items", defaultValue = "false") Boolean inclItems) {
    return new ResponseEntity<>(bookingService.getBookingByBookingExtId(bookingExtId, inclItems),
        HttpStatus.OK);
  }

  @GetMapping("/bookings/{id}/items")
  ResponseEntity<Booking> getBookingItemsByExtId(
      @PathVariable("id") Long eventExtId) {
    return new ResponseEntity<>(bookingService.getBookingByBookingExtId(eventExtId, true),
        HttpStatus.OK);
  }

  @GetMapping("/timecurves/{id}/booking-items")
  ResponseEntity<List<Booking>> getBookingsByGsnRange(
      @PathVariable("id") Long timecurveId,
      @RequestParam(value = "dimension") BookKeepingDimension dimension,
      @RequestParam(value = "item-type", required = false) BookKeepingItemType itemType,
      @RequestParam(value = "item-id", required = false) Long itemId,
      @RequestParam(value = "max-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate maxDate1,
      @RequestParam(value = "max-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate maxDate2,
      @RequestParam(value = "from-gsn") Long fromGsn,
      @RequestParam(value = "to-gsn") Long toGsn) {
    return new ResponseEntity<>(
        new ArrayList<>(bookingService
            .listBookingsForTimecurveIdAndGsnBetween(timecurveId, dimension, itemType, itemId,
                maxDate1, maxDate2, fromGsn, toGsn)),
        HttpStatus.OK);
  }


  @GetMapping("/timecurves/{id}/turnovers")
  ResponseEntity<List<BookingQueryTurnover>> getBookingTurnoverByGsnRange(
      @PathVariable("id") Long timecurveId,
      @RequestParam(value = "dimension") BookKeepingDimension dimension,
      @RequestParam(value = "item-type", required = false) BookKeepingItemType itemType,
      @RequestParam(value = "item-id", required = false) Long itemId,
      @RequestParam(value = "max-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate maxDate1,
      @RequestParam(value = "max-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate maxDate2,
      @RequestParam(value = "from-gsn") Long fromGsn,
      @RequestParam(value = "to-gsn") Long toGsn) {
    return new ResponseEntity<>(
        new ArrayList<>(bookingTurnoverService
            .getBookingTurnoverByGsnRange(timecurveId, dimension, itemType, itemId, maxDate1,
                maxDate2, fromGsn, toGsn)),
        HttpStatus.OK);
  }

}
