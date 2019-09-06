package timecurvemanager.bookkeeping.domain.booking.api;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BookingExternalEvent implements Serializable {

  // correlation-id
  @NotNull
  private String orderId;

  @NotNull
  private Long bookingExtId;

  @NotNull
  private Integer bookingSequenceNr;

  @NotNull
  private String tenantId;

}
