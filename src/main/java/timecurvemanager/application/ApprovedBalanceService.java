package timecurvemanager.application;

import static timecurvemanager.domain.balance.ApprovedBalanceNotFoundException.approvedBalanceNotFound;
import static timecurvemanager.domain.balance.ApprovedBalanceSmallerZeroException.balanceSmallerZero;

import java.math.BigDecimal;
import java.util.HashMap;
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
              BigDecimal.ZERO, BigDecimal.ZERO));
    }
  }

  private void addBalance(HashMap<String, ApprovedBalance> approvedBalanceMap,
      EventItem eventItem) {

    String key = eventItem.getDimension() + ":" + eventItem.getTimecurve().getId() + ":" + eventItem
        .getItemType().abbreviation() + ":" + eventItem.getItemId();

    ApprovedBalance approvedBalance = approvedBalanceMap.get(key);
    if (approvedBalance == null) {
      approvedBalance = getBalanceFromEventItem(eventItem);
    }

    //@todo: lock balance

    // update balance
    BigDecimal newValue = new BigDecimal(0)
        .add(approvedBalance.getValue1().add(eventItem.getValue1()));
    BigDecimal newTover = new BigDecimal(0)
        .add(approvedBalance.getTover1().add(eventItem.getValue1()));

    log.info("NEW BAL: " + newValue + " NEW TOVER: " + newTover);
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
    //@todo implement loop and update
    // check for balance (should be done in a dedicated service)
    if (approvedBalance.getValue1().compareTo(BigDecimal.ZERO) < 0) {
      throw balanceSmallerZero(approvedBalance.getValue1(), approvedBalance.getTover1());
    }
    // SAVE UPDATED BALANCE
    log.info("UPDATE BALANCE: " + approvedBalance.getValue1() + " : " + approvedBalance.getTover1());
    approvedBalanceRepository.save(approvedBalance);
  }

  // MAIN
  public void addEvent(Event newEvent, Event lastEvent) {

    HashMap<String, ApprovedBalance> approvedBalanceMap = new HashMap<>();

    // LAST EVENT
    if (lastEvent != null) {
      lastEvent.getEventItems().stream()
          // check if check on approved balance is needed (on itemType and on timecurve object)
          .filter(
              item -> item.getItemType().buildApprovedBalance()
                  && item.getTimecurve().getNeedBalanceApproval())
          .forEach(e -> {
            addBalance(approvedBalanceMap, e);
          });
    }

    // NEW EVENT
    newEvent.getEventItems().stream()
        // check if check on approved balance is needed (on itemType and on timecurve object)
        .filter(
            item -> item.getItemType().buildApprovedBalance()
                && item.getTimecurve().getNeedBalanceApproval())
        .forEach(e -> {
          addBalance(approvedBalanceMap, e);
        });
    approvedBalanceMap.entrySet().stream()
        .filter(a -> a.getValue().getTover1().compareTo(BigDecimal.ZERO) != 0)
        .forEach(f -> updateBalance(f.getValue()));
  }

}
