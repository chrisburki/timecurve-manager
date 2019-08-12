package timecurvemanager.domain.balance;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.BookKeepingItemType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApprovedBalance {

  private Long id;

  private BookKeepingDimension dimension;

  private Long timecurveId;

  private BookKeepingItemType itemType;

  private Long itemId;

  @Setter
  private BigDecimal value1;

  @Setter
  private BigDecimal tover1;

}
