package timecurvemanager.application;

import static timecurvemanager.domain.balance.ApprovedBalanceChangedException.approvedBalanceChanged;
import static timecurvemanager.domain.balance.ApprovedBalanceNotFoundException.approvedBalanceNotFound;
import static timecurvemanager.domain.balance.ApprovedBalanceSmallerZeroException.balanceSmallerZero;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.balance.ApprovedBalanceRepository;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemType;

@Service
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
        .orElseThrow(() -> approvedBalanceNotFound(dimension, timecurveId, itemType, itemId));
  }

  /*
   * create or update Balance
   * ************************
   * create or updates an approved balance for a timecurve object*/
  private ApprovedBalance saveBalance(EventItem eventItem, BigDecimal value) {
    return approvedBalanceRepository.save(
        new ApprovedBalance(null, eventItem.getDimension(), eventItem.getTimecurve().getId(),
            eventItem.getItemType(), eventItem.getItemId(), value));
  }

  private Optional<ApprovedBalance> getBalance(EventItem eventItem) {
    return approvedBalanceRepository
        .findById(eventItem.getApprovedBalance().getId());
  }

  private Optional<ApprovedBalance> getBalanceFromEventItem(EventItem eventItem) {
    if (eventItem.getApprovedBalance() != null) {
      return getBalance(eventItem);
    } else {
      return Optional.empty();
    }
  }

  private Optional<ApprovedBalance> updateBalance(EventItem eventItem) {

    // GET & UPDATE BALANCe
    // get Approved Balance from EventItem
    Optional<ApprovedBalance> optionalBalance = getBalanceFromEventItem(eventItem);

    if (!optionalBalance.isPresent()) {
      ApprovedBalance balance = saveBalance(eventItem, eventItem.getValue1());
      // get again (for locking)
      optionalBalance = getBalance(eventItem);
    }

    // update balance
    BigDecimal newValue = new BigDecimal(0)
        .add(optionalBalance.get().getValue1().add(eventItem.getValue1()));

    // CHECKS
    // check if evenItem balance is equal to getted balance
    if (!(eventItem.getApprovedBalance() != null
        && newValue.compareTo(eventItem.getApprovedBalance().getValue1()) == 0)) {
      throw approvedBalanceChanged(eventItem.getApprovedBalance().getValue1(), newValue);
    }

    // check for balance (should be done in a dedicated service)
    if (newValue.compareTo(BigDecimal.ZERO) < 0) {
      throw balanceSmallerZero(optionalBalance.get().getValue1(), eventItem.getValue1());
    }

    // SAVE UPDATED BALANCe
    return Optional.of(saveBalance(eventItem, newValue));
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
