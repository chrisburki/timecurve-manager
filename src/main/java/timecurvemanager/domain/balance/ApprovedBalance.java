package timecurvemanager.domain.balance;

import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

import java.math.BigDecimal;

public class ApprovedBalance {

  private Long timecurveObjectId;

  private EventDimension dimension;

  private EventItemType itemType;

  private Long itemId;

  private BigDecimal value1;

}
