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
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;

@Getter
@ToString
@Slf4j
public abstract class BookingMessage implements Serializable {

  private final Long extId;

  private final Integer sequenceNr;

  @NotNull
  private final String orderId;

  @NotNull
  private final String tenantId;

  @NotNull
  private final BookKeepingDimension dimension;

  @NotNull
  private final BookingStatus status;

  @NotNull
  private final String useCase;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private final LocalDate date1;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private final LocalDate date2;

  private final Long gsn;

  @Getter
  private List<BookingItemMessage> bookingItems = new ArrayList<>();

  @Getter
  @AllArgsConstructor
  @ToString
  public class BookingItemMessage implements Serializable {

    @NotNull
    private final Integer rowNr;

    @NotNull
    private final String objectId;

    @NotNull
    private final BookKeepingItemType itemType;

    @NotNull
    private final Long itemId;

    @NotNull
    private final BigDecimal value1;

    private final BigDecimal value2;

    private final BigDecimal value3;

    @NotNull
    private final BigDecimal tover1;

    private final BigDecimal tover2;

    private final BigDecimal tover3;

  }

  public BookingMessage(Long extId, Integer sequenceNr,
      @NotNull String orderId, @NotNull String tenantId,
      @NotNull BookKeepingDimension dimension,
      @NotNull BookingStatus status, @NotNull String useCase,
      @NotNull LocalDate date1, @NotNull LocalDate date2, Long gsn) {
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
      @NotNull String objectId,
      @NotNull BookKeepingItemType itemType,
      @NotNull Long itemId, @NotNull BigDecimal value1, BigDecimal value2,
      BigDecimal value3, @NotNull BigDecimal tover1, BigDecimal tover2, BigDecimal tover3) {
    BookingItemMessage b = new BookingItemMessage(rowNr, objectId, itemType, itemId, value1, value2,
        value3, tover1, tover2, tover3);
    this.bookingItems.add(b);
  }

  public void addBookingItem(BookingItemMessage bookingItem) {
    this.bookingItems.add(bookingItem);
  }

}