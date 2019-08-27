package timecurvemanager.domain.event.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class Event {

  private Long id;

  @Setter
  private Long eventExtId;

  @Setter
  private Integer sequenceNr;

  @NotNull
  private String orderId;

  @NotNull
  private String tenantId;

  @NotNull
  private BookKeepingDimension dimension;

  @NotNull
  private EventStatus status;

  @NotNull
  private String useCase;

  @NotNull
  private LocalDate date1;

  private LocalDate date2;

  private Long gsn;

  private List<EventItem> eventItems = new ArrayList<>();

  public void addEventItem(EventItem eventItem) {
    this.eventItems.add(eventItem);
    eventItem.setEvent(this);
  }

  public void addEventItem2(EventItem eventItem) {
    this.eventItems.add(eventItem);
  }

  public void setIdToNull() {
    this.id = null;
  }

  // internal setting 1 (domain to entity)
  public Event(Long eventExtId, Integer sequenceNr, @NotNull String orderId,
      @NotNull String tenantId, @NotNull BookKeepingDimension dimension,
      @NotNull EventStatus status, @NotNull String useCase, @NotNull LocalDate date1,
      @NotNull LocalDate date2) {
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

  // internal setting 1 (entity to domain)
  public Event(Long id, Long eventExtId, Integer sequenceNr, @NotNull String orderId,
      @NotNull String tenantId, @NotNull BookKeepingDimension dimension,
      @NotNull EventStatus status, @NotNull String useCase, @NotNull LocalDate date1,
      @NotNull LocalDate date2, Long gsn) {
    this.id = id;
    this.eventExtId = eventExtId;
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
  public Event(Long eventExtId, @NotNull String tenantId, @NotNull String orderId,
      @NotNull BookKeepingDimension dimension, @NotNull EventStatus status, @NotNull String useCase,
      @NotNull LocalDate date1, @NotNull LocalDate date2) {
    this.eventExtId = eventExtId;
    this.tenantId = tenantId;
    this.orderId = orderId;
    this.dimension = dimension;
    this.status = status;
    this.useCase = useCase;
    this.date1 = date1;
    this.date2 = date2;
  }
}
