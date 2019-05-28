package timecurvemanager.application;

import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;
import timecurvemanager.domain.timecurveObject.TimecurveObject;

import javax.transaction.Transactional;

public class ApprovedBalanceService {

  /*
   * getApprovedBalance
   * ******************
   * return approved balance. Only supported for itemTypes with no items*/
  public ApprovedBalance getApprovedBalance(Long timecurveId, EventDimension dimension,
      EventItemType itemType) {
    return null;
  }

  @Transactional
  public ApprovedBalance addEvent(Event event) {
    return null;
  }

}
