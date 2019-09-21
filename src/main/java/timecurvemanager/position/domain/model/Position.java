package timecurvemanager.position.domain.model;

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

  public Position(@NotNull String tenantId,
      @NotNull String containerId, String tag, String name,
      @NotNull PositionValueType valueType,
      @NotNull String valueTag, Boolean doBalanceCheck) {
    this.tenantId = tenantId;
    this.containerId = containerId;
    this.tag = tag;
    this.name = name;
    this.valueType = valueType;
    this.valueTag = valueTag;
    this.doBalanceCheck = doBalanceCheck;
  }

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
