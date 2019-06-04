package timecurvemanager.domain.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import timecurvemanager.domain.balance.ApprovedBalance;
import timecurvemanager.domain.timecurveObject.TimecurveObject;

//@Todo: replace primary key id with event, rowNr

public class EventItem {

  private Long id;

  private Event event;

  private Integer rowNr;

  private String tenantId;

  private EventDimension dimension;

  private TimecurveObject timecurve;

  private EventItemType itemType;

  private Long itemId;

  private LocalDate date1;

  private LocalDate date2;

  private BigDecimal value1;

  private BigDecimal value2;

  private BigDecimal value3;

  private BigDecimal tover1;

  private BigDecimal tover2;

  private BigDecimal tover3;

  private ApprovedBalance approvedBalance;

  public EventItem(Long id, Event event, Integer rowNr, String tenantId,
      EventDimension dimension, TimecurveObject timecurve,
      EventItemType itemType, Long itemId, LocalDate date1, LocalDate date2,
      BigDecimal value1, BigDecimal value2, BigDecimal value3, BigDecimal tover1,
      BigDecimal tover2, BigDecimal tover3, ApprovedBalance approvedBalance) {
    this.id = id;
    this.event = event;
    this.rowNr = rowNr;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.timecurve = timecurve;
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
    this.approvedBalance = approvedBalance;
  }

  public Long getId() {
    return id;
  }

  public Event getEvent() {
    return event;
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

  public TimecurveObject getTimecurve() {
    return timecurve;
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

  public ApprovedBalance getApprovedBalance() {
    return approvedBalance;
  }

  @Override
  public String toString() {
    return "EventItem{" +
        "id=" + id +
        ", event=" + event +
        ", rowNr=" + rowNr +
        ", tenantId='" + tenantId + '\'' +
        ", dimension=" + dimension +
        ", timecurve=" + timecurve +
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
        ", approvedBalance=" + approvedBalance +
        '}';
  }
}
