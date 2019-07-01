package timecurvemanager.application;

import static timecurvemanager.domain.balance.ApprovedBalanceNotFoundException.approvedBalanceNotFound;
import static timecurvemanager.domain.balance.ApprovedBalanceSmallerZeroException.balanceSmallerZero;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.balance.ApprovedBalanceRepository;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemType;

@Service
@Slf4j
public class ApprovedBalanceService {

  private final ApprovedBalanceRepository approvedBalanceRepository;
  private final TimecurveObjectService timecurveObjectService;

  public ApprovedBalanceService(
      ApprovedBalanceRepository approvedBalanceRepository,
      TimecurveObjectService timecurveObjectService) {
    this.approvedBalanceRepository = approvedBalanceRepository;
    this.timecurveObjectService = timecurveObjectService;
  }

  /*
   * getApprovedBalance
   * ******************
   * return approved balance. Only supported for itemTypes with no items*/
  public ApprovedBalance getApprovedBalance(EventDimension dimension, Long timecurveId,
      EventItemType itemType, Long itemId) {
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

  private ApprovedBalance getBalanceFromEventItem(EventItem eventItem) {
    if (eventItem.getApprovedBalance() != null) {
      return getBalance(eventItem.getApprovedBalance().getId());
    } else {
      return approvedBalanceRepository
          .findByDimensionAndTimecurveIdAndItemTypeAndItemId(eventItem.getDimension(),
              eventItem.getTimecurve().getId(), eventItem.getItemType(), eventItem.getItemId())
          .orElse(new ApprovedBalance(null, eventItem.getDimension(),
              eventItem.getTimecurve().getId(), eventItem.getItemType(), eventItem.getItemId(),
              BigDecimal.ZERO));
    }
  }

  private ApprovedBalance updateBalance(EventItem eventItem) {

    // get Approved Balance from EventItem
    ApprovedBalance approvedBalance = getBalanceFromEventItem(eventItem);

    //@todo: lock balance

    // update balance
    BigDecimal newValue = new BigDecimal(0)
        .add(approvedBalance.getValue1().add(eventItem.getValue1()));

    log.info("NEW BAL: " + newValue);
    // CHECKS
    // check if evenItem balance is equal to getted balance
//    if (!(eventItem.getApprovedBalance() != null
//        && newValue.compareTo(eventItem.getApprovedBalance().getValue1()) == 0)
//    ) {
//      throw approvedBalanceChanged(eventItem.getApprovedBalance().getValue1(), newValue);
//    }

    // check for balance (should be done in a dedicated service)
    if (newValue.compareTo(BigDecimal.ZERO) < 0) {
      throw balanceSmallerZero(approvedBalance.getValue1(), eventItem.getValue1());
    }

    approvedBalance.setValue1(newValue);
    // SAVE UPDATED BALANCE
    return approvedBalanceRepository.save(approvedBalance);
  }

  // MAIN
  public void addEvent(Event event) {
    event.getEventItems().stream()
        // check if check on approved balance is needed (on itemType and on timecurve object)
        .filter(
            item -> item.getItemType().buildApprovedBalance()
                && item.getTimecurve().getNeedBalanceApproval())
        .forEach(e -> {
          updateBalance(e);
        });
  }

}
