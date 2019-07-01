package timecurvemanager.infrastructure.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import timecurvemanager.application.EventItemService;
import timecurvemanager.application.EventService;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemType;

@RestController
@RequestMapping("/timecurve")
public class EventController {

  private final EventService eventService;
  private final EventItemService eventItemService;

  public EventController(EventService eventService,
      EventItemService eventItemService) {
    this.eventService = eventService;
    this.eventItemService = eventItemService;
  }

  @GetMapping("/events")
  ResponseEntity<List<Event>> listObjects(@RequestParam("dimension") EventDimension dimension,
      @RequestParam(value = "fromDate1", required = false) LocalDate fromDate1,
      @RequestParam(value = "toDate1", required = false) LocalDate toDate1,
      @RequestParam(value = "fromDate2", required = false) LocalDate fromDate2,
      @RequestParam(value = "toDate2", required = false) LocalDate toDate2,
      @RequestParam(value = "useCase", required = false) String useCase
  ) {
    return new ResponseEntity<>(
        new ArrayList<>(
            eventService.listEvents(dimension, fromDate1, toDate1, fromDate2, toDate2, useCase)),
        HttpStatus.OK);
  }

  @GetMapping("/eventItems")
  ResponseEntity<List<EventItem>> listObjects(@RequestParam("dimension") EventDimension dimension,
      @RequestParam(value = "objectId", required = false) Long objectId,
      @RequestParam(value = "itemType", required = false) EventItemType itemType,
      @RequestParam(value = "itemId", required = false) Long itemId,
      @RequestParam(value = "fromDate1", required = false) LocalDate fromDate1,
      @RequestParam(value = "toDate1", required = false) LocalDate toDate1,
      @RequestParam(value = "fromDate2", required = false) LocalDate fromDate2,
      @RequestParam(value = "toDate2", required = false) LocalDate toDate2,
      @RequestParam(value = "useCase", required = false) String useCase
  ) {
    return new ResponseEntity<>(
        new ArrayList<>(
            eventItemService
                .listEventItems(dimension, objectId, itemType, itemId, fromDate1, toDate1,
                    fromDate2, toDate2, useCase)),
        HttpStatus.OK);
  }

  @GetMapping("/events/{id}")
  ResponseEntity<Event> getEventByExtId(
      @PathVariable("id") Long eventExtId,
      @RequestParam(value = "includeItems", defaultValue = "false") Boolean inclItems) {
    return new ResponseEntity<>(eventService.getEventByEventExtId(eventExtId, inclItems),
        HttpStatus.OK);
  }

  @GetMapping("/events/{id}/items")
  ResponseEntity<List<EventItem>> getEventItemsByExtId(
      @PathVariable("id") Long eventExtId) {
    return new ResponseEntity<>(
        new ArrayList<>(eventItemService.getEventItemsByEventExtId(eventExtId)), HttpStatus.OK);
  }

  @PostMapping("/events")
  ResponseEntity<Event> addEvent(@RequestBody Event event) {
    Event result = eventService.addEvent(event);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
