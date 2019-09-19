package timecurvemanager.bookkeeping.application;

import static timecurvemanager.bookkeeping.domain.balance.ApprovedBalanceNotFoundException.approvedBalanceNotFound;
import static timecurvemanager.bookkeeping.domain.balance.ApprovedBalanceSmallerZeroException.balanceSmallerZero;

import java.math.BigDecimal;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.bookkeeping.domain.balance.ApprovedBalance;
import timecurvemanager.bookkeeping.domain.balance.ApprovedBalanceRepository;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;
import timecurvemanager.bookkeeping.domain.booking.model.BookingItem;

@Service
@Slf4j
public class ApprovedBalanceService {

  private final ApprovedBalanceRepository approvedBalanceRepository;
  private final TimecurveService timecurveService;

  public ApprovedBalanceService(
      ApprovedBalanceRepository approvedBalanceRepository,
      TimecurveService timecurveService) {
    this.approvedBalanceRepository = approvedBalanceRepository;
    this.timecurveService = timecurveService;
  }

  /*
   * getApprovedBalance
   * ******************
   * return approved balance. Only supported for itemTypes with no items*/
  public ApprovedBalance getApprovedBalance(BookKeepingDimension dimension, Long timecurveId,
      BookKeepingItemType itemType, Long itemId) {
    return approvedBalanceRepository
        .findByDimensionAndTimecurveIdAndItemTypeAndItemId(dimension, timecurveId, itemType, itemId)
        .orElseThrow(() -> approvedBalanceNotFound(null, dimension, timecurveId, itemType, itemId));
  }

  /*
   * create or update Balance
   * ************************
   * create or updates an approved balance for a timecurve object*/

  private ApprovedBalance getBalance(Long id) {
    return approvedBalanceRepository
        .findById(id).orElseThrow(() -> approvedBalanceNotFound(id, null, null, null, null));
  }

  private ApprovedBalance getBalanceFromEventItem(Booking booking, BookingItem bookingItem) {
    return approvedBalanceRepository
        .findByDimensionAndTimecurveIdAndItemTypeAndItemId(booking.getDimension(),
            bookingItem.getTimecurveId(), bookingItem.getItemType(), bookingItem.getItemId())
        .orElse(new ApprovedBalance(null, booking.getDimension(),
            bookingItem.getTimecurveId(), bookingItem.getItemType(), bookingItem.getItemId(),
            BigDecimal.ZERO, BigDecimal.ZERO));
  }

  private void addBalance(HashMap<String, ApprovedBalance> approvedBalanceMap, Booking booking,
      BookingItem bookingItem) {

    String key = booking.getDimension() + ":" + bookingItem.getTimecurveId() + ":" + bookingItem
        .getItemType().abbreviation() + ":" + bookingItem.getItemId();

    ApprovedBalance approvedBalance = approvedBalanceMap.get(key);
    if (approvedBalance == null) {
      approvedBalance = getBalanceFromEventItem(booking, bookingItem);
    }

    // update balance
    BigDecimal newValue = new BigDecimal(0)
        .add(approvedBalance.getValue1().add(bookingItem.getValue1()));
    BigDecimal newTover = new BigDecimal(0)
        .add(approvedBalance.getTover1().add(bookingItem.getValue1()));

    log.debug("NEW BAL: " + newValue + " NEW TOVER: " + newTover);
    // CHECKS
    // check if evenItem balance is equal to getted balance
//    if (!(eventItem.getApprovedBalance() != null
//        && newValue.compareTo(eventItem.getApprovedBalance().getValue1()) == 0)
//    ) {
//      throw approvedBalanceChanged(eventItem.getApprovedBalance().getValue1(), newValue);
//    }

    approvedBalance.setValue1(newValue);
    approvedBalance.setTover1(newTover);
    approvedBalanceMap.put(key, approvedBalance);

  }

  private void updateBalance(ApprovedBalance approvedBalance) {
    // check for balance (should be done in a dedicated service)
    if (approvedBalance.getValue1().compareTo(BigDecimal.ZERO) < 0) {
      throw balanceSmallerZero(approvedBalance.getValue1(), approvedBalance.getTover1());
    }
    // SAVE UPDATED BALANCE
    log.debug(
        "UPDATE BALANCE: " + approvedBalance.getValue1() + " : " + approvedBalance.getTover1());
    approvedBalanceRepository.save(approvedBalance);
  }

  // MAIN
  public void addBooking(Booking newBooking, Booking lastBooking) {

    HashMap<String, ApprovedBalance> approvedBalanceMap = new HashMap<>();

    // LAST EVENT
    if (lastBooking != null) {
      lastBooking.getBookingItems().stream()
          // check if check on approved balance is needed (on itemType and on timecurve object)
          .filter(
              item -> item.getItemType().buildApprovedBalance()
                  && timecurveService.getById(item.getTimecurveId()).getNeedBalanceApproval())
          .forEach(item -> {
            addBalance(approvedBalanceMap, lastBooking, item);
          });
    }

    // NEW EVENT
    newBooking.getBookingItems().stream()
        // check if check on approved balance is needed (on itemType and on timecurve object)
        .filter(
            item -> item.getItemType().buildApprovedBalance()
                && timecurveService.getById(item.getTimecurveId()).getNeedBalanceApproval())
        .forEach(item -> {
          addBalance(approvedBalanceMap, newBooking, item);
        });

    approvedBalanceMap.entrySet().stream()
        .filter(a -> a.getValue().getTover1().compareTo(BigDecimal.ZERO) != 0)
        .forEach(f -> updateBalance(f.getValue()));
  }

}
