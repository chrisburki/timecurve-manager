package timecurvemanager.position.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Position {

  private Long id;

  @NotNull
  private String tenantId;

  @NotNull
  private String containerId;

  // @NotNull
  private String tag;

  // @NotNull
  private String name;

  @NotNull
  private PositionValueType valueType;

  @NotNull
  private String valueTag;

  // @NotNull
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
