package timecurvemanager.bookkeeping.domain.booking.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Booking {

  private Long id;

  @Setter
  private Long bookingExtId;

  @Setter
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

  private Long gsn;

  private List<BookingItem> bookingItems = new ArrayList<>();

  public void addBookingItem(BookingItem bookingItem) {
    this.bookingItems.add(bookingItem);
  }

  public void setIdToNull() {
    this.id = null;
  }

  // internal setting 1 (domain to entity)
  public Booking(Long bookingExtId, Integer sequenceNr, @NotNull String orderId,
      @NotNull String tenantId, @NotNull BookKeepingDimension dimension,
      @NotNull BookingStatus status, @NotNull String useCase, @NotNull LocalDate date1,
      @NotNull LocalDate date2) {
    this.bookingExtId = bookingExtId;
    this.sequenceNr = sequenceNr;
    this.orderId = orderId;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.status = status;
    this.useCase = useCase;
    this.date1 = date1;
    this.date2 = date2;
  }

  // internal setting 1 (entity to domain)
  public Booking(Long id, Long bookingExtId, Integer sequenceNr, @NotNull String orderId,
      @NotNull String tenantId, @NotNull BookKeepingDimension dimension,
      @NotNull BookingStatus status, @NotNull String useCase, @NotNull LocalDate date1,
      @NotNull LocalDate date2, Long gsn) {
    this.id = id;
    this.bookingExtId = bookingExtId;
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

  // for external setting
  public Booking(Long bookingExtId, @NotNull String tenantId, @NotNull String orderId,
      @NotNull BookKeepingDimension dimension, @NotNull BookingStatus status,
      @NotNull String useCase, @NotNull LocalDate date1, @NotNull LocalDate date2) {
    this.bookingExtId = bookingExtId;
    this.tenantId = tenantId;
    this.orderId = orderId;
    this.dimension = dimension;
    this.status = status;
    this.useCase = useCase;
    this.date1 = date1;
    this.date2 = date2;
  }
}
