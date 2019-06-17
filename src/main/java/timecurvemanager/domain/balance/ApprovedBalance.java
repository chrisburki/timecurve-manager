package timecurvemanager.domain.balance;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@Getter
@AllArgsConstructor
@ToString
public class ApprovedBalance {

  private Long id;

  private EventDimension dimension;

  private Long timecurveId;

  private EventItemType itemType;

  private Long itemId;

  private BigDecimal value1;

}
