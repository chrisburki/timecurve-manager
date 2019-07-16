package timecurvemanager.domain.gsn;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Gsn {

  @NotNull
  Long sequenceNr;

  @NotNull
  LocalDateTime gsnDate;

}
