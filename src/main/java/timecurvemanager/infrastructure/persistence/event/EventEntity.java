package timecurvemanager.infrastructure.persistence.event;

import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "event", indexes = {
    @Index(name = "idx_event_id", columnList = "event_ext_id, sequence_nr", unique = true),
//    @Index(name = "idx_event_date1", columnList = "date1", unique = false),
//    @Index(name = "idx_event_date2", columnList = "date2", unique = false)
})
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "event_ext_id")
  private Long eventExtId;

  @Column(name = "sequence_nr")
  private Integer sequenceNr;

  @Column(name = "tenant_id")
  private String tenantId;

  private EventDimension dimension;

  private EventStatus status;

  @Column(name = "use_case")
  private String useCase;

  private LocalDate date1;

  private LocalDate date2;

  public EventEntity() {
  }

  public EventEntity(Long eventExtId, Integer sequenceNr, String tenantId, EventDimension dimension,
      EventStatus status, String useCase, LocalDate date1, LocalDate date2) {
    this.eventExtId = eventExtId;
    this.sequenceNr = sequenceNr;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.status = status;
    this.useCase = useCase;
    this.date1 = date1;
    this.date2 = date2;
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

  @Override
  public String toString() {
    return "EventEntity{" +
        "id=" + id +
        ", eventExtId=" + eventExtId +
        ", sequenceNr=" + sequenceNr +
        ", tenantId='" + tenantId + '\'' +
        ", dimension=" + dimension +
        ", status=" + status +
        ", useCase='" + useCase + '\'' +
        ", date1=" + date1 +
        ", date2=" + date2 +
        '}';
  }
}
