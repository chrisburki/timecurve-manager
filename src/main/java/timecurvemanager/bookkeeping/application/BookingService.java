package timecurvemanager.bookkeeping.application;

import static timecurvemanager.bookkeeping.domain.booking.model.BookingAddException.bookingAddException;
import static timecurvemanager.bookkeeping.domain.booking.model.BookingClearingNotZeroException.bookingClearingNotZero;
import static timecurvemanager.bookkeeping.domain.booking.model.BookingItemGsnBiggerApprovedException.bookingItemGsnBiggerApproved;
import static timecurvemanager.bookkeeping.domain.booking.model.BookingNotFoundException.bookingNotFound;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.Collection;

import java.util.HashMap;

import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import timecurvemanager.bookkeeping.domain.booking.BookingMessaging;
import timecurvemanager.bookkeeping.domain.booking.BookingRepository;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingDomainEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingMessage;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;
import timecurvemanager.bookkeeping.domain.booking.model.BookingItem;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;
import timecurvemanager.bookkeeping.domain.timecurve.Timecurve;

@Service
@Slf4j
public class BookingService {

  private static final String primaryKey = "Primary Key (Id)";
  private static final String extBookingId = "Booking Id (external)";

  public static final LocalDate minDate = LocalDate.of(1900, 01, 01);
  public static final LocalDate maxDate = LocalDate.of(4712, 12, 31);

  private final BookingRepository bookingRepository;
  private final ApprovedBalanceService approvedBalanceService;
  private final TimecurveService timecurveService;
  private final BookingMessaging bookingMessaging;

  public BookingService(BookingRepository bookingRepository,
      ApprovedBalanceService approvedBalanceService,
      TimecurveService timecurveService,
      BookingMessaging bookingMessaging) {
    this.bookingRepository = bookingRepository;
    this.approvedBalanceService = approvedBalanceService;
    this.timecurveService = timecurveService;
    this.bookingMessaging = bookingMessaging;
  }

  //
  // helper
  //
  <T> T nvl(T arg0, T arg1) {
    return (arg0 == null) ? arg1 : arg0;
  }

  //
  // query
  //

  /*
   * Search for existing booking(s) based on id - primary key - probably not needed
   * ******************************************************************************
   * */
  public Booking getBookingById(Long id) {
    return bookingRepository.findById(id).orElseThrow(() -> bookingNotFound(id, primaryKey));
  }

  /*
   * Search for latest version of booking based on booking id - external
   * *******************************************************************
   * */
  public Booking getBookingByBookingExtId(Long bookingId, Boolean inclItems) {
    if (inclItems) {
      return bookingRepository.findQueryByBookingExtId(bookingId)
          .orElseThrow(() -> bookingNotFound(bookingId, extBookingId));
    } else {
      return bookingRepository.findLastByBookingExtId(bookingId)
          .orElseThrow(() -> bookingNotFound(bookingId, extBookingId));
    }
  }

  /*
   * List bookings
   * *************
   * */
  public Collection<Booking> listBookings(BookKeepingDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase, Long maxGsn) {

    LocalDate fDate1 = nvl(fromDate1, minDate);
    LocalDate tDate1 = nvl(toDate1, maxDate);
    LocalDate fDate2 = nvl(fromDate2, minDate);
    LocalDate tDate2 = nvl(toDate2, maxDate);

    return bookingRepository
        .findQueryBookings(dimension, fDate1, tDate1, fDate2, tDate2, useCase, maxGsn);
  }

  /*
   * List bookings for a timecurve
   * *****************************
   * */
  public Collection<Booking> listBookingsForTimecurve(
      BookKeepingDimension dimension, Long timecurveId, BookKeepingItemType itemType, Long itemId,
      LocalDate fromDate1, LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2,
      String useCase, Long maxGsn) {
    LocalDate fDate1 = nvl(fromDate1, minDate);
    LocalDate tDate1 = nvl(toDate1, maxDate);
    LocalDate fDate2 = nvl(fromDate2, minDate);
    LocalDate tDate2 = nvl(toDate2, maxDate);

    return bookingRepository
        .findQueryBookingItems(dimension, timecurveId, itemType, itemId, fDate1, tDate1, fDate2,
            tDate2, useCase, maxGsn);
  }

  /*
   * List bookings for a timecurve and GSN Range
   * *******************************************
   * */
  public Collection<Booking> listBookingsForTimecurveIdAndGsnBetween(
      Long timecurveId, BookKeepingDimension dimension, BookKeepingItemType itemType, Long itemId,
      LocalDate maxDate1, LocalDate maxDate2, Long fromGsn, Long toGsn) {
    LocalDate tDate1 = nvl(maxDate1, maxDate);
    LocalDate tDate2 = nvl(maxDate2, maxDate);

    return bookingRepository
        .findQueryByTimecurveIdAndGsnBetween(timecurveId, dimension, itemType, itemId, tDate1,
            tDate2, fromGsn, toGsn);
  }

  //
  // Post
  //

  /* check clearing (1. part of addBooking) */
  private void buildClearing(HashMap<String, BigDecimal> clearingMap, String
      clearingReference,
      BigDecimal value) {
    clearingMap.put(clearingReference, clearingMap
        .getOrDefault(clearingReference, BigDecimal.ZERO).add(value));
  }

  private void evalClearing(Booking booking) {
    HashMap<String, BigDecimal> clearingMap = new HashMap<>();
    //1. Build Clearing Map
    booking.getBookingItems().stream()
        .filter(
            bookingItem -> timecurveService.getById(bookingItem.getTimecurveId()).needClearing())
        .forEach(bookingItem -> {
          buildClearing(clearingMap,
              timecurveService.getById(bookingItem.getTimecurveId()).getClearingReference(),
              bookingItem.getValue1());
        });
    // 2. Check Clearing Map for != 0
    clearingMap.entrySet().stream().filter(e -> e.getValue().compareTo(BigDecimal.ZERO) != 0)
        .forEach(f -> {
          throw bookingClearingNotZero(f.getKey(), f.getValue());
        });
  }

  /* find last booking & reverse it (2. part of addBooking) */
  private BookingItem reverseItem(BookingItem item) {
    item.setIdToNull();
    item.setValue1(item.getValue1().negate());
    if (item.getValue2() != null) {
      item.setValue2(item.getValue2().negate());
    }
    if (item.getValue3() != null) {
      item.setValue3(item.getValue3().negate());
    }
    item.setTover1(item.getTover1().negate());
    if (item.getValue2() != null) {
      item.setTover2(item.getValue2().negate());
    }
    if (item.getValue3() != null) {
      item.setTover3(item.getValue3().negate());
    }
    return item;
  }

  private Booking getLastBooking(Long evtExtId) {
    Booking booking = getBookingByBookingExtId(evtExtId, true);
    log.debug("LAST Booking: " + booking.getBookingExtId() + " : " + booking.getSequenceNr());
    booking.setSequenceNr(-booking.getSequenceNr());
    booking.setIdToNull();
    booking.getBookingItems().forEach(bookingItem -> reverseItem(bookingItem));
    return booking;
  }

  private void addBookingExtId(Booking booking, Long bookingExtId, Integer sequenceNr) {
    booking.setBookingExtId(bookingExtId);
    booking.setSequenceNr(sequenceNr);
  }

  /* check approved gsn (3. part of addBooking) */
  private class ApprovedToverGsn {

    BigDecimal tover;
    Long lastGsn;
    BookKeepingDimension dimension;
    BookKeepingItemType itemType;
    Long itemId;
  }

  private void addApprovedToverAndGsn(HashMap<Long, ApprovedToverGsn> approvedBalanceGsnMap,
      Booking booking, BookingItem bookingItem) {

    // update or add tover
    ApprovedToverGsn approvedToverGsn = approvedBalanceGsnMap.get(bookingItem.getTimecurveId());
    if (approvedToverGsn != null) {
      approvedToverGsn.tover = new BigDecimal(0)
          .add(approvedToverGsn.tover.add(bookingItem.getValue1()));
    } else {
      approvedToverGsn = new ApprovedToverGsn();
      approvedToverGsn.tover = bookingItem.getValue1();
      approvedToverGsn.dimension = booking.getDimension();
      approvedToverGsn.itemType = bookingItem.getItemType();
      approvedToverGsn.itemId = bookingItem.getItemId();
    }

    // add to hash map
    approvedBalanceGsnMap.put(bookingItem.getTimecurveId(), approvedToverGsn);
  }

  private void checkGsn(Long timecurveId, ApprovedToverGsn approvedToverGsn, Long approvedGsn) {
    Long lastGsn = bookingRepository
        .findQueryLastGsnByTimecurve(timecurveId, approvedToverGsn.dimension,
            approvedToverGsn.itemType, approvedToverGsn.itemId);
//    EventItem foundEventItem = eventItemRepository
//        .findFirstByTimecurveEntityIdAndDimensionAndItemTypeOrderByGsnDescEventEntityIdDesc(timecurveId,
//            approvedToverGsn.dimension, approvedToverGsn.itemType).orElse(new EventItem());
    if (lastGsn != null && approvedGsn < lastGsn) {
      throw bookingItemGsnBiggerApproved(lastGsn, approvedGsn);
    }
  }

  private void checkApprovedToverGsn(Booking newBooking, Booking lastBooking, Long approvedGsn) {

    // if apprvedGsn is null then don't do any checks
    if (approvedGsn == null) {
      return;
    }

    // map per timecurveId -> only one check per timecurveId supported
    HashMap<Long, ApprovedToverGsn> approvedToverGsnMap = new HashMap<>();

    // LAST Booking
    if (lastBooking != null) {
      lastBooking.getBookingItems().stream()
          // check if check on approved balance is needed (on itemType and on timecurve object)
          .filter(
              item -> item.getItemType().buildApprovedBalance()
                  && timecurveService.getById(item.getTimecurveId()).getNeedBalanceApproval())
          .forEach(item -> {
            addApprovedToverAndGsn(approvedToverGsnMap, lastBooking, item);
          });
    }

    // NEW Booking
    newBooking.getBookingItems().stream()
        // check if check on approved balance is needed (on itemType and on timecurve object)
        .filter(
            item -> item.getItemType().buildApprovedBalance()
                && timecurveService.getById(item.getTimecurveId()).getNeedBalanceApproval())
        .forEach(item -> {
          addApprovedToverAndGsn(approvedToverGsnMap, newBooking, item);
        });

    approvedToverGsnMap.entrySet().stream()
        .filter(a -> a.getValue().tover.compareTo(BigDecimal.ZERO) != 0)
        .forEach(f -> checkGsn(f.getKey(), f.getValue(), approvedGsn));
  }

  /* put booking (last & new) (4. part of addBooking) */
  private Booking putBooking(Booking booking, Booking lastBooking) {
    /*reveres last booking if not null*/
    if (lastBooking != null) {
      try {
        bookingRepository.save(lastBooking);
      } catch (DataAccessException d) {
        throw bookingAddException(lastBooking.getId(), lastBooking.getBookingExtId(),
            lastBooking.getSequenceNr());
      }
    }

    /*insert new one with higher sequenceNr*/
    try {
      return bookingRepository.save(booking);
    } catch (DataAccessException d) {
      throw bookingAddException(booking.getId(), booking.getBookingExtId(),
          booking.getSequenceNr());
    }
  }

  private void publishItems(BookingDomainEvent bookingDomainEvent, Booking booking,
      BookingItem item) {
    String objectId = timecurveService
        .getObjectByTimecuveIdAndDate(item.getTimecurveId(), booking.getDate1());
    bookingDomainEvent.createBookingItem(
        item.getRowNr(), objectId, item.getItemType(), item.getItemId(), item.getValue1(),
        item.getValue2(), item.getValue3(), item.getTover1(), item.getTover2(), item.getTover3(),
        item.getTimecurveId());
  }


  private void publishDomainEvent(Booking booking) {
    BookingDomainEvent bookingDomainEvent =
        BookingDomainEvent.builder()
            .extId(booking.getBookingExtId())
            .sequenceNr(booking.getSequenceNr())
            .orderId(booking.getOrderId())
            .tenantId(booking.getTenantId())
            .dimension(booking.getDimension())
            .status(booking.getStatus())
            .useCase(booking.getUseCase())
            .date1(booking.getDate1())
            .date2(booking.getDate2())
            .gsn(booking.getGsn())
            .build();
    booking.getBookingItems().forEach(item -> publishItems(bookingDomainEvent, booking, item));

    this.bookingMessaging.sendDomainEvent(bookingDomainEvent);
  }

  /* update balance 6. part of addBooking) */
  private Booking relvUpdate(Booking booking) {
    if (booking != null && (booking.getStatus().equals(BookingStatus.APPROVED) || booking
        .getStatus().equals(BookingStatus.BOOKED))) {
      return booking;
    }
    return null;
  }

  private void updateBalance(Booking newBooking, Booking lastBooking) {
    approvedBalanceService.addBooking(newBooking, lastBooking);
  }

  /*
   * Add Booking. If existing reverse items and reinsert new ones.
   * Additional check clearing consistency.
   * *******************************************************************
   * */
  @Transactional
  public Booking addBooking(Booking booking, Long approvedGsn) {
    log.debug("Approved GSN: " + approvedGsn);
    // 1. evaluate clearing
    evalClearing(booking);

    // 2. find BookingExtId and if not existing add one
    Booking lastBooking = null;
    if (booking.getBookingExtId() == null) {
      addBookingExtId(booking, bookingRepository.getNextBookingExtId(), 1);
    } else {
      lastBooking = getLastBooking(booking.getBookingExtId());
      log.debug(
          "NEW BOOKING: " + lastBooking.getBookingExtId() + " : " + (-lastBooking.getSequenceNr()
              + 1));
      addBookingExtId(booking, lastBooking.getBookingExtId(), -lastBooking.getSequenceNr() + 1);
    }

    // 3. check approved gsn
    checkApprovedToverGsn(relvUpdate(booking), relvUpdate(lastBooking), approvedGsn);

    // 4. add booking & items
    booking = putBooking(booking, lastBooking);

    // 5. messaging booking -- may replace with outbox table concept
    publishDomainEvent(booking);

    //@todo: remove as update in baltov done async via message
    // 6. check approved gsn for relevant timecurves & update balance
    updateBalance(relvUpdate(booking), relvUpdate(lastBooking));

    return booking;
  }

  //
  // Command
  //

  private void addBookingItems(Booking booking, BookingMessage.BookingItemMessage item) {
    Timecurve timecurve = timecurveService
        .addTimecurve(item.getObjectId(), booking.getDate1());
    BookingItem bookingItem = BookingItem.builder()
        .rowNr(item.getRowNr())
        .timecurveId(timecurve.getId())
        .itemType(item.getItemType())
        .itemId(item.getItemId())
        .value1(item.getValue1())
        .value2(item.getValue2())
        .value3(item.getValue3())
        .tover1(item.getTover1())
        .tover2(item.getTover2())
        .tover3(item.getTover3())
        .build();
    booking.addBookingItem(bookingItem);
  }

  /*
   * Process Command
   * ***************
   * */
  public BookingExternalEvent processBookingCommand(BookingCommand bookingCommand) {
    //@todo: check for duplicates
    Booking booking = new Booking(
        bookingCommand.getExtId(),
        bookingCommand.getSequenceNr(),
        bookingCommand.getOrderId(),
        bookingCommand.getTenantId(),
        bookingCommand.getDimension(),
        bookingCommand.getStatus(),
        bookingCommand.getUseCase(),
        bookingCommand.getDate1(),
        bookingCommand.getDate2());
    bookingCommand.getBookingItems().forEach(bookingItemMessage -> addBookingItems(booking,
        bookingItemMessage));
    Booking newBooking = addBooking(booking, bookingCommand.getGsn());
    return BookingExternalEvent.builder()
        .orderId(newBooking.getOrderId())
        .bookingExtId(newBooking.getBookingExtId())
        .bookingSequenceNr(newBooking.getSequenceNr())
        .tenantId(newBooking.getTenantId())
        .build();
  }
}
