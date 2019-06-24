package timecurvemanager.domain.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Event {

  //@todo: handle gsn
  //@todo: external only eventExtId needed (for adding),
  // id, sequenceNr is derived from already existing ones

  private Long id;

  @Setter
  private Long eventExtId;

  @Setter
  private Integer sequenceNr;

  @NotNull
  private String tenantId;

  @NotNull
  private EventDimension dimension;

  @NotNull
  private EventStatus status;

  @NotNull
  private String useCase;

  @NotNull
  private LocalDate date1;

  @NotNull
  private LocalDate date2;

  private List<EventItem> eventItems = new ArrayList<>();

  public void addEventItem(EventItem eventItem) {
    this.eventItems.add(eventItem);
    eventItem.setEvent(this);
  }

  public Event(Long id, Long eventExtId, Integer sequenceNr,
      @NotNull String tenantId,
      @NotNull EventDimension dimension,
      @NotNull EventStatus status, @NotNull String useCase,
      @NotNull LocalDate date1, LocalDate date2) {
    this.id = id;
    this.eventExtId = eventExtId;
    this.sequenceNr = sequenceNr;
    this.tenantId = tenantId;
    this.dimension = dimension;
    this.status = status;
    this.useCase = useCase;
    this.date1 = date1;
    this.date2 = date2;
  }
}
