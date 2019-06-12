package timecurvemanager.domain.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Event {

  //@todo: handle gsn


  //@todo: external only eventExtId needed (for adding), id, sequenceNr is derived from already existing ones
  private Long id;

  private Long eventExtId;

  private Integer sequenceNr;

  private String tenantId;

  private EventDimension dimension;

  private EventStatus status;

  private String useCase;

  private LocalDate date1;

  private LocalDate date2;

  private List<EventItem> eventItems;

  public Event(Long id, Long eventExtId, Integer sequenceNr, String tenantId,
      EventDimension dimension, EventStatus status, String useCase, LocalDate date1,
      LocalDate date2, List<EventItem> eventItems) {
    this.id = id;
    this.eventExtId = eventExtId;
    this.sequenceNr = sequenceNr;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.status = status;
    this.useCase = useCase;
    this.date1 = date1;
    this.date2 = date2;
    this.eventItems = eventItems;
    this.eventItems = new ArrayList<>();
  }

  public void addEventItem(EventItem eventItem) {
    this.eventItems.add(eventItem);
  }

  public Long getId() {
    return id;
  }

  public Long getEventExtId() {
    return eventExtId;
  }

  public Integer getSequenceNr() {
    return sequenceNr;
  }

  public String getTenantId() {
    return tenantId;
  }

  public EventDimension getDimension() {
    return dimension;
  }

  public EventStatus getStatus() {
    return status;
  }

  public String getUseCase() {
    return useCase;
  }

  public LocalDate getDate1() {
    return date1;
  }

  public LocalDate getDate2() {
    return date2;
  }

  public List<EventItem> getEventItems() {
    return eventItems;
  }

  @Override
  public String toString() {
    return "Event{" +
        "id=" + id +
        ", eventExtId=" + eventExtId +
        ", sequenceNr=" + sequenceNr +
        ", tenantId='" + tenantId + '\'' +
        ", dimension=" + dimension +
        ", status=" + status +
        ", useCase='" + useCase + '\'' +
        ", date1=" + date1 +
        ", date2=" + date2 +
        ", eventItems=" + eventItems +
        '}';
  }
}
