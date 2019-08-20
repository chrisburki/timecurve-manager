package timecurvemanager.infrastructure.persistence.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.EventStatus;


@Entity
@Table(name = "event", indexes = {
    @Index(name = "idx_event_id", columnList = "event_ext_id, sequence_nr", unique = true)
//    @Index(name = "idx_event_date1", columnList = "date1", unique = false),
//    @Index(name = "idx_event_date2", columnList = "date2", unique = false)
})
@Getter
@NoArgsConstructor
@ToString
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "event_ext_id")
  @NotNull
  private Long eventExtId;

  @Column(name = "sequence_nr")
  @NotNull
  private Integer sequenceNr;

  @Column(name = "order_id")
  @NotNull
  private String orderId;

  @Column(name = "tenant_id")
  @NotNull
  private String tenantId;

  @NotNull
  private BookKeepingDimension dimension;

  @NotNull
  private EventStatus status;

  @Column(name = "use_case")
  @NotNull
  private String useCase;

  @NotNull
  private LocalDate date1;

  private LocalDate date2;

  private Long gsn;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventEntity", orphanRemoval = true)
  private List<EventItemEntity> eventItems = new ArrayList<>();

  @PrePersist
  public void onPrePersist() {
    gsn = currGsn();
  }

  private Long currGsn() {
    final Long shift = 100000L;
    LocalDateTime currDateTime = LocalDateTime.now();
    return currDateTime.toLocalDate().getLong(ChronoField.EPOCH_DAY) * shift + currDateTime
        .getLong(ChronoField.SECOND_OF_DAY);
  }

  public EventEntity(Long eventExtId, Integer sequenceNr, String orderId, String tenantId, BookKeepingDimension dimension,
      EventStatus status, String useCase, LocalDate date1, LocalDate date2) {
    this.eventExtId = eventExtId;
    this.sequenceNr = sequenceNr;
    this.orderId = orderId;
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
