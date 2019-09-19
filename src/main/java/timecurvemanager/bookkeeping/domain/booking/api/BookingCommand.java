package timecurvemanager.bookkeeping.domain.booking.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;

public class BookingCommand extends BookingMessage implements Serializable {

  @Builder
  public BookingCommand(Long extId, Integer sequenceNr,
      @NotNull String orderId, @NotNull String tenantId, @NotNull BookKeepingDimension dimension,
      @NotNull BookingStatus status, @NotNull String useCase, @NotNull LocalDate date1,
      @NotNull LocalDate date2, Long gsn) {
    super(extId, sequenceNr, orderId, tenantId, dimension, status, useCase, date1, date2, gsn);
  }

  @Getter
  @ToString
  public class BookingItemCommandMessage extends BookingMessage.BookingItemMessage implements
      Serializable {
    public BookingItemCommandMessage(@NotNull Integer rowNr,
        @NotNull String objectId, @NotNull BookKeepingItemType itemType, @NotNull Long itemId,
        @NotNull BigDecimal value1, BigDecimal value2, BigDecimal value3,
        @NotNull BigDecimal tover1, BigDecimal tover2, BigDecimal tover3) {
      super(rowNr, objectId, itemType, itemId, value1, value2, value3, tover1, tover2, tover3);
    }
  }

  public void createBookingItem(@NotNull Integer rowNr,
      @NotNull String objectId, @NotNull BookKeepingItemType itemType, @NotNull Long itemId,
      @NotNull BigDecimal value1, BigDecimal value2, BigDecimal value3, @NotNull BigDecimal tover1,
      BigDecimal tover2, BigDecimal tover3) {
    BookingCommand.BookingItemCommandMessage b = new BookingCommand.BookingItemCommandMessage(rowNr,
        objectId, itemType, itemId, value1, value2, value3, tover1, tover2, tover3);
    this.addBookingItem(b);
  }
}
