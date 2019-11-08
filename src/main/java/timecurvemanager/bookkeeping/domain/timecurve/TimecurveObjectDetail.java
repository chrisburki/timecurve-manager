package timecurvemanager.bookkeeping.domain.timecurve;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import timecurvemanager.position.domain.model.PositionValueType;

@Getter
@ToString
@Builder
public class TimecurveObjectDetail {

  @NotNull
  private String objectId;

  @NotNull
  private String tenantId;

  @NotNull
  private PositionValueType valueType;

  @NotNull
  private String valueTag;

  @NotNull
  private Boolean doBalanceCheck;

  public String getClearingReference() {
    if (this.getValueType() == PositionValueType.CURRENCY) {
      return this.getValueTag();
    } else {
      return null;
    }
  }

  public Boolean getNeedBalanceApproval() {
    return this.getDoBalanceCheck() && this.getValueType() == PositionValueType.CURRENCY;
  }

}
