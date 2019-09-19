package timecurvemanager.bookkeeping.infrastructure.persistence.booking;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;

@Entity
@Table(name = "booking_item",
    indexes = {
        @Index(name = "idx_gsn_timecurve", columnList = "gsn,timecurve_id", unique = false),
        @Index(name = "idx_item_booking", columnList = "booking_id", unique = false),
//    @Index(name = "idx_item_timecurve_date1", columnList = "timecurve_id, date1", unique = false),
//    @Index(name = "idx_item_timecurve_date2", columnList = "timecurve_id, date2", unique = false)
    }
)
@Getter
@NoArgsConstructor
@ToString
public class BookingItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
  @Setter
  @NotNull
  private BookingEntity bookingEntity;

  @Column(name = "row_nr")
  @NotNull
  private Integer rowNr;

  @Column(name = "tenant_id")
  @NotNull
  private String tenantId;

  @NotNull
  private BookKeepingDimension dimension;

  @Column(name = "timecurve_id")
  @NotNull
  private Long timecurveId;

  @Column(name = "item_type")
  @NotNull
  private BookKeepingItemType itemType;

  @Column(name = "item_id")
  @NotNull
  private Long itemId;

  @NotNull
  private LocalDate date1;

  @NotNull
  private LocalDate date2;

  @NotNull
  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  @NotNull
  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;

  private Long gsn;

  @PrePersist
  public void onPrePersist() {
    gsn = bookingEntity.getGsn();
  }

  public BookingItemEntity(Integer rowNr, String tenantId,
      BookKeepingDimension dimension,
      Long timecurveId, BookKeepingItemType itemType, Long itemId,
      LocalDate date1, LocalDate date2, BigDecimal value1, BigDecimal value2,
      BigDecimal value3, BigDecimal tover1, BigDecimal tover2, BigDecimal tover3) {
    this.rowNr = rowNr;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.timecurveId = timecurveId;
    this.itemType = itemType;
    this.itemId = itemId;
    this.date1 = date1;
    this.date2 = date2;
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
    this.tover1 = tover1;
    this.tover2 = tover2;
    this.tover3 = tover3;
  }
}
