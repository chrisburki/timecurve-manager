package timecurvemanager.domain.position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Position {

  private Long id;

  // @NotNull
  private String tenantId;

  // @NotNull
  private String containerId;

  // @NotNull
  private String tag;

  // @NotNull
  private String name;

  // @NotNull
  private PositionValueType valueType;

  // @NotNull
  private String valueTag;
}