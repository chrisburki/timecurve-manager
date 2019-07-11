package timecurvemanager.infrastructure.persistence.position;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.domain.position.PositionValueType;

@Entity
@Table(name = "position", indexes = @Index(name = "idx_position_tag",
    columnList = "tag", unique = true))
@Getter
@NoArgsConstructor
@ToString
public class PositionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "tenant_id")
  @NotNull
  private String tenantId;

  @NotNull
  private String containerId;

  @NotNull
  private String tag;

  @NotNull
  private String name;

  @Column(name = "value_type")
  @NotNull
  private PositionValueType valueType;

  @Column(name = "value_tag")
  @NotNull
  private String valueTag;

  @Column(name = "do_balance_check")
  @NotNull
  private Boolean doBalanceCheck;

  public PositionEntity(@NotNull String tenantId,
      @NotNull String containerId, @NotNull String tag,
      @NotNull String name,
      @NotNull PositionValueType valueType,
      @NotNull String valueTag, @NotNull Boolean doBalanceCheck) {
    this.tenantId = tenantId;
    this.containerId = containerId;
    this.tag = tag;
    this.name = name;
    this.valueType = valueType;
    this.valueTag = valueTag;
    this.doBalanceCheck = doBalanceCheck;
  }
}
