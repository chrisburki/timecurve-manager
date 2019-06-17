package timecurvemanager.domain.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
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

  public void addEventItem(EventItem eventItem) {
    this.eventItems.add(eventItem);
    eventItem.setEvent(this);
  }

}
