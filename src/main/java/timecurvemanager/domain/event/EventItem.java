package timecurvemanager.domain.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
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

  @NotNull
  private String tenantId;

  @NotNull
  private EventDimension dimension;

  @NotNull
  private TimecurveObject timecurve;

  @NotNull
  private EventItemType itemType;

  private Long itemId;

  @NotNull
  private LocalDate date1;

  @NotNull
  private LocalDate date2;

  @NotNull
  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  @NotNull
  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;

  private ApprovedBalance approvedBalance;

}
