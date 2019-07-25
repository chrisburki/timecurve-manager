package timecurvemanager.infrastructure.persistence.event;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

import timecurvemanager.infrastructure.persistence.timecurve.TimecurveEntity;

@Entity
@Table(name = "event_item"
//    , indexes = {
//        @Index(name = "idx_item_event_entity", columnList = "eventEntity", unique = false),
//    @Index(name = "idx_item_timecurve_date1", columnList = "timecurve_id, date1", unique = false),
//    @Index(name = "idx_item_timecurve_date2", columnList = "timecurve_id, date2", unique = false)
//}
)
@Getter
@NoArgsConstructor
@ToString
public class EventItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @Setter
  @NotNull
  private EventEntity eventEntity;

  @Column(name = "row_nr")
  @NotNull
  private Integer rowNr;

  @Column(name = "tenant_id")
  @NotNull
  private String tenantId;

  @NotNull
  private EventDimension dimension;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "timecurve_id", referencedColumnName = "id", nullable = false)
  @NotNull
  private TimecurveEntity timecurveEntity;

  @Column(name = "item_type")
  @NotNull
  private EventItemType itemType;

  @Column(name = "item_id")
  private Long itemId;

  @NotNull
  private LocalDate date1;

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
    gsn = eventEntity.getGsn();
  }

  public EventItemEntity(Integer rowNr, String tenantId,
      EventDimension dimension,
      TimecurveEntity timecurveEntity, EventItemType itemType, Long itemId,
      LocalDate date1, LocalDate date2, BigDecimal value1, BigDecimal value2,
      BigDecimal value3, BigDecimal tover1, BigDecimal tover2, BigDecimal tover3) {
    this.rowNr = rowNr;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.timecurveEntity = timecurveEntity;
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
