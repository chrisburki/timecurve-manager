package timecurvemanager.application;

import static timecurvemanager.domain.balance.ApprovedBalanceNotFoundException.approvedBalanceNotFound;
import static timecurvemanager.domain.balance.ApprovedBalanceSmallerZeroException.balanceSmallerZero;

import java.math.BigDecimal;
import java.util.Optional;
import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.balance.ApprovedBalanceRepository;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemType;

import javax.transaction.Transactional;

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
//@todo: how does pessimistic locking works if item is new?
  private ApprovedBalance saveBalance(EventItem eventItem, BigDecimal value) {
    return approvedBalanceRepository.save(
        new ApprovedBalance(null, eventItem.getDimension(), eventItem.getTimecurve().getId(),
            eventItem.getItemType(), eventItem.getItemId(), value));
  }

  private Optional<ApprovedBalance> getBalance(EventItem eventItem) {
    return approvedBalanceRepository
        .findById(eventItem.getApprovedBalance().getId());
  }

  private Optional<ApprovedBalance> updateBalance(EventItem eventItem) {
    // check if check on approved balance is needed (on itemType and on timecurve object)
    if (eventItem.getItemType().buildApprovedBalance() && eventItem.getTimecurve().getNeedBalanceApproval()) {

      Optional<ApprovedBalance> optionalBalance = getBalance(eventItem);

      if (!optionalBalance.isPresent()) {
        ApprovedBalance balance = saveBalance(eventItem, eventItem.getValue1());
        // get again (for locking)
        optionalBalance = getBalance(eventItem);
      }

      BigDecimal newValue = new BigDecimal(0)
          .add(optionalBalance.get().getValue1().add(eventItem.getValue1()));

      // check for balance
      if (newValue.compareTo(BigDecimal.ZERO) < 0) {
        throw balanceSmallerZero(optionalBalance.get().getValue1(), eventItem.getValue1());
      }
      return Optional.of(saveBalance(eventItem, newValue));

    } else {
      return Optional.empty();
    }
  }

  @Transactional
  public void addEvent(Event event) {
    event.getEventItems().forEach(eventItem -> {
      updateBalance(eventItem);
    });
  }

}
