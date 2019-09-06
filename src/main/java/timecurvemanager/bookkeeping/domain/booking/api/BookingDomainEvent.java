package timecurvemanager.bookkeeping.domain.booking.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;


@Getter
@ToString
@Slf4j
public class BookingDomainEvent extends BookingMessage implements Serializable {

  @Builder
  public BookingDomainEvent(Long extId, Integer sequenceNr,
      @NotNull String orderId, @NotNull String tenantId,
      @NotNull BookKeepingDimension dimension,
      @NotNull BookingStatus status, @NotNull String useCase,
      @NotNull LocalDate date1, @NotNull LocalDate date2, Long gsn) {
    super(extId, sequenceNr, orderId, tenantId, dimension, status, useCase, date1, date2, gsn);
  }

  @Getter
  @ToString
  public class BookingItemDomainEventMessage extends BookingMessage.BookingItemMessage implements
      Serializable {

    @NotNull
    private final Long timecurveId;

    public BookingItemDomainEventMessage(@NotNull Integer rowNr,
        @NotNull String objectId, @NotNull BookKeepingItemType itemType, @NotNull Long itemId,
        @NotNull BigDecimal value1, BigDecimal value2, BigDecimal value3,
        @NotNull BigDecimal tover1, BigDecimal tover2, BigDecimal tover3,
        @NotNull Long timecurveId) {
      super(rowNr, objectId, itemType, itemId, value1, value2, value3, tover1, tover2, tover3);
      this.timecurveId = timecurveId;
    }
  }

  public void createBookingItem(@NotNull Integer rowNr,
      @NotNull String objectId, @NotNull BookKeepingItemType itemType, @NotNull Long itemId,
      @NotNull BigDecimal value1, BigDecimal value2, BigDecimal value3, @NotNull BigDecimal tover1,
      BigDecimal tover2, BigDecimal tover3, @NotNull Long timecurveId) {
    BookingItemDomainEventMessage b = new BookingItemDomainEventMessage(rowNr, objectId, itemType,
        itemId, value1, value2, value3, tover1, tover2, tover3, timecurveId);
    this.addBookingItem(b);
  }

}
