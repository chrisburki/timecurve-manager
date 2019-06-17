package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventStatus;


@Entity
@Table(name = "event", indexes = {
    @Index(name = "idx_event_id", columnList = "event_ext_id, sequence_nr", unique = true),
//    @Index(name = "idx_event_date1", columnList = "date1", unique = false),
//    @Index(name = "idx_event_date2", columnList = "date2", unique = false)
})
@Getter
@NoArgsConstructor
@ToString
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

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventEntity", orphanRemoval = true)
  private List<EventItemEntity> eventItems = new ArrayList<>();

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

  public void addEventItem(EventItemEntity eventItem) {
    this.eventItems.add(eventItem);
    eventItem.setEventEntity(this);
  }

}
