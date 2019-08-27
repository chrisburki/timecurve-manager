package timecurvemanager.domain.event.messaging;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class EventReplyMessage implements Serializable {

  // correlation-id
  @NotNull
  private String orderId;

  @NotNull
  private Long eventExtId;

  @NotNull
  private Integer eventSequenceNr;

  @NotNull
  private String tenantId;

}
