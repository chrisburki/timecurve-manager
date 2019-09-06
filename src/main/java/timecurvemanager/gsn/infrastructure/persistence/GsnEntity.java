package timecurvemanager.gsn.infrastructure.persistence;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "gsn")
@Getter
@NoArgsConstructor
@ToString
public class GsnEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  @NotNull
  Long id;

  @Column(name = "gsn_date")
  @NotNull
  LocalDateTime gsnDate;

  public GsnEntity(@NotNull LocalDateTime gsnDate) {
    this.gsnDate = gsnDate;
  }
}
