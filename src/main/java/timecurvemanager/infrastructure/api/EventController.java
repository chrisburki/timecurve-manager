package timecurvemanager.infrastructure.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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
import timecurvemanager.application.EventTurnoverService;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.BookKeepingItemType;
import timecurvemanager.domain.event.model.Event;
import timecurvemanager.domain.event.model.EventItem;
import timecurvemanager.domain.event.view.EventTurnover;

@RestController
@RequestMapping("/book-keeping")
public class EventController {

  private final EventService eventService;
  private final EventItemService eventItemService;
  private final EventTurnoverService eventTurnoverService;

  public EventController(EventService eventService,
      EventItemService eventItemService,
      EventTurnoverService eventTurnoverService) {
    this.eventService = eventService;
    this.eventItemService = eventItemService;
    this.eventTurnoverService = eventTurnoverService;
  }

  @GetMapping("/events")
  ResponseEntity<List<Event>> listEvents(@RequestParam("dimension") BookKeepingDimension dimension,
      @RequestParam(value = "from-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate1,
      @RequestParam(value = "to-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate1,
      @RequestParam(value = "from-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate2,
      @RequestParam(value = "to-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate2,
      @RequestParam(value = "use-case", required = false) String useCase
  ) {
    return new ResponseEntity<>(
        new ArrayList<>(
            eventService.listEvents(dimension, fromDate1, toDate1, fromDate2, toDate2, useCase)),
        HttpStatus.OK);
  }

  @PostMapping("/events")
  ResponseEntity<Event> addEvent(@RequestBody Event event,
      @RequestParam(value = "approved-gsn", required = false) Long approvedGsn) {
    Event result = eventService.addEvent(event, approvedGsn);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/event-items")
  ResponseEntity<List<EventItem>> listObjects(
      @RequestParam("dimension") BookKeepingDimension dimension,
      @RequestParam(value = "timecurve-id", required = false) Long timecurveId,
      @RequestParam(value = "item-type", required = false) BookKeepingItemType itemType,
      @RequestParam(value = "item-id", required = false) Long itemId,
      @RequestParam(value = "from-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate1,
      @RequestParam(value = "to-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate1,
      @RequestParam(value = "from-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate2,
      @RequestParam(value = "to-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate2,
      @RequestParam(value = "use-case", required = false) String useCase
  ) {
    return new ResponseEntity<>(
        new ArrayList<>(
            eventItemService
                .listEventItems(dimension, timecurveId, itemType, itemId, fromDate1, toDate1,
                    fromDate2, toDate2, useCase)),
        HttpStatus.OK);
  }

  @GetMapping("/events/{id}")
  ResponseEntity<Event> getEventByExtId(
      @PathVariable("id") Long eventExtId,
      @RequestParam(value = "include-items", defaultValue = "false") Boolean inclItems) {
    return new ResponseEntity<>(eventService.getEventByEventExtId(eventExtId, inclItems),
        HttpStatus.OK);
  }

  @GetMapping("/events/{id}/items")
  ResponseEntity<List<EventItem>> getEventItemsByExtId(
      @PathVariable("id") Long eventExtId) {
    return new ResponseEntity<>(
        new ArrayList<>(eventItemService.getEventItemsByEventExtId(eventExtId)), HttpStatus.OK);
  }

  @GetMapping("/timecurves/{id}/event-items")
  ResponseEntity<List<EventItem>> getEventItemsByGsnRange(
      @PathVariable("id") Long timecurveId,
      @RequestParam(value = "dimension") BookKeepingDimension dimension,
      @RequestParam(value = "item-type", required = false) BookKeepingItemType itemType,
      @RequestParam(value = "item-id", required = false) Long itemId,
      @RequestParam(value = "max-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate maxDate1,
      @RequestParam(value = "max-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate maxDate2,
      @RequestParam(value = "from-gsn") Long fromGsn, @RequestParam(value = "to-gsn") Long toGsn) {
    return new ResponseEntity<>(
        new ArrayList<>(eventItemService
            .getEventItemByGsn(timecurveId, dimension, itemType, itemId, maxDate1, maxDate2,
                fromGsn, toGsn)),
        HttpStatus.OK);
  }

  @GetMapping("/timecurves/{id}/turnovers")
  ResponseEntity<List<EventTurnover>> getEventTurnoverByGsnRange(
      @PathVariable("id") Long timecurveId,
      @RequestParam(value = "dimension") BookKeepingDimension dimension,
      @RequestParam(value = "item-type", required = false) BookKeepingItemType itemType,
      @RequestParam(value = "item-id", required = false) Long itemId,
      @RequestParam(value = "max-date1", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate maxDate1,
      @RequestParam(value = "max-date2", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate maxDate2,
      @RequestParam(value = "from-gsn") Long fromGsn, @RequestParam(value = "to-gsn") Long toGsn) {
    return new ResponseEntity<>(
        new ArrayList<>(eventTurnoverService
            .getEventTurnoverByGsnRange(timecurveId, dimension, itemType, itemId, maxDate1,
                maxDate2, fromGsn, toGsn)),
        HttpStatus.OK);
  }

}
