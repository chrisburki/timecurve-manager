package timecurvemanager.bookkeeping.domain.booking.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;

public class BookingQueryDetail {

  private Long bookingExtId;

  @NotNull
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
  private LocalDate date1;

  @NotNull
  private LocalDate date2;

  @NotNull
  private Integer itemRowNr;

  @NotNull
  private Long timecurveId;

  @NotNull
  private String objectId;

  @NotNull
  private BookKeepingItemType itemType;

  @NotNull
  private Long itemId;

  @NotNull
  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  @NotNull
  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;

  private Long gsn;

}
