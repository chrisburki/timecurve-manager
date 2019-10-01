package timecurvemanager.bookkeeping.domain.booking.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Slf4j
public class BookingCommand implements Serializable {

  private Long extId;

  private Integer sequenceNr;

  @NotNull
  private String orderId;

  @NotNull
  private String tenantId;

  @NotNull
  private BookKeepingDimension dimension;

  @NotNull
  private BookingStatus status;

  @NotNull
  private String useCase;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate date1;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate date2;

  private Long gsn;

  @Getter
  @Setter
  private List<BookingCommandItem> bookingItems = new ArrayList<>();

  @Builder
  public BookingCommand(Long extId, Integer sequenceNr,
      @NotNull String orderId, @NotNull String tenantId, @NotNull BookKeepingDimension dimension,
      @NotNull BookingStatus status, @NotNull String useCase, @NotNull LocalDate date1,
      @NotNull LocalDate date2, Long gsn) {
    this.extId = extId;
    this.sequenceNr = sequenceNr;
    this.orderId = orderId;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.status = status;
    this.useCase = useCase;
    this.date1 = date1;
    this.date2 = date2;
    this.gsn = gsn;
  }



  public void createBookingItem(@NotNull Integer rowNr,
      @NotNull String objectId, @NotNull BookKeepingItemType itemType, @NotNull Long itemId,
      @NotNull BigDecimal value1, BigDecimal value2, BigDecimal value3, @NotNull BigDecimal tover1,
      BigDecimal tover2, BigDecimal tover3) {
    BookingCommandItem b = new BookingCommandItem(rowNr, objectId, itemType, itemId, value1, value2,
        value3, tover1, tover2, tover3);
    this.bookingItems.add(b);
  }
}
