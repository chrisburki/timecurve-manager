package timecurvemanager.domain.event.messaging;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.BookKeepingItemType;
import timecurvemanager.domain.event.EventStatus;

@Getter
@Builder
@ToString
public class EventMessage implements Serializable {

  @Setter
  private Long eventExtId;

  @Setter
  private Integer eventSequenceNr;

  @NotNull
  private String orderId;

  @NotNull
  private Integer eventItemRowNr;

  @NotNull
  private String tenantId;

  @NotNull
  private BookKeepingDimension dimension;

  @NotNull
  private Long timecurveId;

  @NotNull
  private BookKeepingItemType itemType;

  @NotNull
  private Long itemId;

  @NotNull
  @JsonFormat(pattern="yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
//  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate date1;

  @NotNull
  @JsonFormat(pattern="yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
//  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate date2;

  @NotNull
  private String objectId;

  @NotNull
  @Setter
  private BigDecimal value1;

  @Setter
  private BigDecimal value2;

  @Setter
  private BigDecimal value3;

  @NotNull
  @Setter
  private BigDecimal tover1;

  @Setter
  private BigDecimal tover2;

  @Setter
  private BigDecimal tover3;

  @NotNull
  private EventStatus status;

  @NotNull
  private String useCase;

  @Setter
  private Long gsn;
}
