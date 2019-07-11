package timecurvemanager.domain.balance;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApprovedBalance {

  private Long id;

  private EventDimension dimension;

  private Long timecurveId;

  private EventItemType itemType;

  private Long itemId;

  @Setter
  private BigDecimal value1;

  @Setter
  private BigDecimal tover1;

}
