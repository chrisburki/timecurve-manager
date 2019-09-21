package timecurvemanager.position.domain.api;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PositionExternalEvent {

  @NotNull
  private Long id;

  @NotNull
  private String tag;

  @NotNull
  private String name;

  @NotNull
  private Boolean needBalanceCheck;

}
