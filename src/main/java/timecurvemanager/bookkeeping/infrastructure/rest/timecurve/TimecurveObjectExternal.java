package timecurvemanager.bookkeeping.infrastructure.rest.timecurve;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.position.domain.model.PositionValueType;


@Getter
@Setter
@ToString
public class TimecurveObjectExternal implements Serializable {
  private Long id;
  private String tenantId;
  private String containerId;
  private String tag;
  private String name;
  private PositionValueType valueType;
  private String valueTag;
  private Boolean doBalanceCheck;
}
