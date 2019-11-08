package timecurvemanager.bookkeeping.domain.booking.model;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class BookingTimecurveDetail {

  @NotNull
  private Long id;

  @NotNull
  private String objectId;

  @NotNull
  private Boolean needClearing;

  @NotNull
  private String clearingReference;

  @NotNull
  private Boolean needBalanceApproval;

}
