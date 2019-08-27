package timecurvemanager.domain.event.messaging;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import timecurvemanager.domain.event.model.BookKeepingItemType;

@Getter
@Builder
@ToString
public class BookingItemMessage implements Serializable {

  @NotNull
  private Integer rowNr;

  @NotNull
  private String objectId;

  @NotNull
  private BookKeepingItemType itemType;

  @NotNull
  private Long itemId;

  @NotNull
  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  @NotNull
  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;

}
