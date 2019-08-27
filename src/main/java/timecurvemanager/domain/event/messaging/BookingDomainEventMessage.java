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
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.BookKeepingItemType;
import timecurvemanager.domain.event.model.EventStatus;

@Getter
@Builder
@ToString
public class BookingDomainEventMessage implements Serializable {

  @NotNull
  private Long eventExtId;

  @NotNull
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
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
//  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate date1;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
//  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate date2;

  @NotNull
  private String objectId;

  @NotNull
  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  @NotNull
  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;

  @NotNull
  private EventStatus status;

  @NotNull
  private String useCase;

  @NotNull
  private Long gsn;
}
