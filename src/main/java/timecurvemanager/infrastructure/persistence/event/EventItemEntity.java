package timecurvemanager.infrastructure.persistence.event;

import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import timecurvemanager.infrastructure.persistence.timecurveObject.TimecurveObjectEntity;

@Entity
@Table(name = "event_item"
//    , indexes = {
//        @Index(name = "idx_item_event_entity", columnList = "eventEntity", unique = false),
//    @Index(name = "idx_item_timecurve_date1", columnList = "timecurve_id, date1", unique = false),
//    @Index(name = "idx_item_timecurve_date2", columnList = "timecurve_id, date2", unique = false)
//}
)
public class EventItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private EventEntity eventEntity;

  @Column(name = "row_nr")
  private Integer rowNr;

  @Column(name = "tenant_id")
  private String tenantId;

  private EventDimension dimension;

  @ManyToOne
  private TimecurveObjectEntity timecurveEntity;

  @Column(name = "item_type")
  private EventItemType itemType;

  @Column(name = "item_id")
  private Long itemId;

  private LocalDate date1;

  private LocalDate date2;

  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;

  public EventItemEntity() {
  }

  public EventItemEntity(EventEntity eventEntity, Integer rowNr, String tenantId,
      EventDimension dimension,
      TimecurveObjectEntity timecurveEntity, EventItemType itemType, Long itemId,
      LocalDate date1, LocalDate date2, BigDecimal value1, BigDecimal value2,
      BigDecimal value3, BigDecimal tover1, BigDecimal tover2, BigDecimal tover3) {
    this.eventEntity = eventEntity;
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

  public Long getId() {
    return id;
  }

  public EventEntity getEventEntity() {
    return eventEntity;
  }

  public Integer getRowNr() {
    return rowNr;
  }

  public String getTenantId() {
    return tenantId;
  }

  public EventDimension getDimension() {
    return dimension;
  }

  public TimecurveObjectEntity getTimecurveEntity() {
    return timecurveEntity;
  }

  public EventItemType getItemType() {
    return itemType;
  }

  public Long getItemId() {
    return itemId;
  }

  public LocalDate getDate1() {
    return date1;
  }

  public LocalDate getDate2() {
    return date2;
  }

  public BigDecimal getValue1() {
    return value1;
  }

  public BigDecimal getValue2() {
    return value2;
  }

  public BigDecimal getValue3() {
    return value3;
  }

  public BigDecimal getTover1() {
    return tover1;
  }

  public BigDecimal getTover2() {
    return tover2;
  }

  public BigDecimal getTover3() {
    return tover3;
  }

  @Override
  public String toString() {
    return "EventItemEntity{" +
        "id=" + id +
        ", eventEntity=" + eventEntity +
        ", rowNr=" + rowNr +
        ", tenantId='" + tenantId + '\'' +
        ", dimension=" + dimension +
        ", timecurveEntity=" + timecurveEntity +
        ", itemType=" + itemType +
        ", itemId=" + itemId +
        ", date1=" + date1 +
        ", date2=" + date2 +
        ", value1=" + value1 +
        ", value2=" + value2 +
        ", value3=" + value3 +
        ", tover1=" + tover1 +
        ", tover2=" + tover2 +
        ", tover3=" + tover3 +
        '}';
  }
}
