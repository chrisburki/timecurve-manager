package timecurvemanager.gsn.domain;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Gsn {

  Long id;

  @NotNull
  LocalDateTime gsnDate;

  public Gsn(@NotNull LocalDateTime gsnDate) {
    this.gsnDate = gsnDate;
  }
}
