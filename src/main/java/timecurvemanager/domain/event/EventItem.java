package timecurvemanager.domain.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.timecurveobject.TimecurveObject;

//@todo: replace primary key id with event, rowNr

@Getter
@AllArgsConstructor
@ToString
public class EventItem {

  private Long id;

  @Setter
  private Event event;

  private Integer rowNr;

  private String tenantId;

  private EventDimension dimension;

  private TimecurveObject timecurve;

  private EventItemType itemType;

  private Long itemId;

  private LocalDate date1;

  private LocalDate date2;

  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;

  private ApprovedBalance approvedBalance;

}
