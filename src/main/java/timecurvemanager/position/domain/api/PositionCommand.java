package timecurvemanager.position.domain.api;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import timecurvemanager.position.domain.model.PositionValueType;

@Builder
@Getter
@ToString
@Slf4j
public class PositionCommand implements Serializable {

  @NotNull
  private String tenantId;

  @NotNull
  private String containerId;

  // @NotNull
  private String tag;

  @NotNull
  private PositionValueType valueType;

  @NotNull
  private String valueTag;

}
