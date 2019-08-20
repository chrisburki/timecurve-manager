package timecurvemanager.application;

import static timecurvemanager.domain.event.EventClearingNotZeroException.eventClearingNotZero;
import static timecurvemanager.domain.event.EventItemGsnBiggerApprovedException.EventItemGsnBiggerApproved;
import static timecurvemanager.domain.event.EventNotFoundException.eventNotFound;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import java.util.HashMap;

import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.BookKeepingDimension;
import timecurvemanager.domain.event.BookKeepingItemType;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemRepository;

import timecurvemanager.domain.event.EventMessaging;
import timecurvemanager.domain.event.EventRepository;
import timecurvemanager.domain.event.EventStatus;
import timecurvemanager.domain.event.messaging.EventMessage;
import timecurvemanager.domain.objecttimecurve.ObjectTimecurveRelation;

@Service
@Slf4j
public class EventService {

  private static final String primaryKey = "Primary Key (Id)";
  private static final String extEventId = "Event Id (external)";

  public static final LocalDate minDate = LocalDate.of(1900, 01, 01);
  public static final LocalDate maxDate = LocalDate.of(4712, 12, 31);

  private final EventRepository eventRepository;
  private final EventItemRepository eventItemRepository;
  private final ApprovedBalanceService approvedBalanceService;
  private final ObjectTimecurveRelationService objectTimecurveRelationService;
  private final timecurvemanager.domain.event.EventMessaging eventMessaging;

  public EventService(EventRepository eventRepository,
      EventItemRepository eventItemRepository,
      ApprovedBalanceService approvedBalanceService,
      ObjectTimecurveRelationService objectTimecurveRelationService,
      EventMessaging eventMessaging) {
    this.eventRepository = eventRepository;
    this.eventItemRepository = eventItemRepository;
    this.approvedBalanceService = approvedBalanceService;
    this.objectTimecurveRelationService = objectTimecurveRelationService;
    this.eventMessaging = eventMessaging;
  }

  /*
   * Helper - nvl
   */
  <T> T nvl(T arg0, T arg1) {
    return (arg0 == null) ? arg1 : arg0;
  }

  /*
   * searchEvents
   * ************
   */

  /* Search for existing event(s) based on id - primary key - probably not needed*/
  public Event getEventById(Long id) {
    return eventRepository.findById(id).orElseThrow(() -> eventNotFound(id, primaryKey));
  }

  /* Search for latest version of event based on event id - external*/
  public Event getEventByEventExtId(Long eventId, Boolean inclItems) {
    int cntItems = 0;
    List<EventItem> eventItems = new ArrayList<>();
    if (inclItems) {
      eventItems = eventItemRepository.findQueryByEventExtId(eventId);
      cntItems = eventItems.size();
    }
    if (cntItems == 0) {
      return eventRepository.findLastByEventExtId(eventId)
          .orElseThrow(() -> eventNotFound(eventId, extEventId));
    } else {
      Event event = eventItems.get(0).getEvent();
      eventItems.stream().forEach(eventItem -> {
        eventItem.setEvent(null);
        event.addEventItem2(eventItem);
      });
      return event;
    }
  }

  /* List events */
  public Collection<Event> listEvents(BookKeepingDimension dimension, LocalDate fromDate1,
      LocalDate toDate1, LocalDate fromDate2, LocalDate toDate2, String useCase) {

    LocalDate fDate1 = nvl(fromDate1, minDate);
    LocalDate tDate1 = nvl(toDate1, maxDate);
    LocalDate fDate2 = nvl(fromDate2, minDate);
    LocalDate tDate2 = nvl(toDate2, maxDate);

    return eventRepository.findQueryEvents(dimension, fDate1, tDate1, fDate2, tDate2, useCase);
  }

  /*
   * add event
   * *********
   */

  //
  /* check clearing (1. part of addEvent) */
  private void buildClearing(HashMap<String, BigDecimal> clearingMap, String
      clearingReference,
      BigDecimal value) {
    clearingMap.put(clearingReference, clearingMap
        .getOrDefault(clearingReference, BigDecimal.ZERO).add(value));
  }

  private void evalClearing(Event event) {
    HashMap<String, BigDecimal> clearingMap = new HashMap<>();
    //1. Build Clearing Map
    event.getEventItems().stream()
        .filter(eventItem -> eventItem.getTimecurve().getClearingReference() != null)
        .forEach(eventItem -> {
          buildClearing(clearingMap, eventItem.getTimecurve().getClearingReference(),
              eventItem.getValue1());
        });
    // 2. Check Clearing Map for != 0
    clearingMap.entrySet().stream().filter(e -> e.getValue().compareTo(BigDecimal.ZERO) != 0)
        .forEach(f -> {
          throw eventClearingNotZero(f.getKey(), f.getValue());
        });
  }

  //
  /* find last event & reverse it (2. part of addEvent) */
  private EventItem reverseItem(EventItem item) {
    item.setIdToNull();
    item.setValue1(item.getValue1().negate());
    if (item.getValue2() != null) {
      item.setValue2(item.getValue2().negate());
    }
    if (item.getValue3() != null) {
      item.setValue3(item.getValue3().negate());
    }
    item.setTover1(item.getTover1().negate());
    if (item.getValue2() != null) {
      item.setTover2(item.getValue2().negate());
    }
    if (item.getValue3() != null) {
      item.setTover3(item.getValue3().negate());
    }
    return item;
  }

  private Event getLastEvent(Long evtExtId) {
    Event event = getEventByEventExtId(evtExtId, true);
    log.debug("LAST EVENT: " + event.getEventExtId() + " : " + event.getSequenceNr());
    event.setSequenceNr(-event.getSequenceNr());
    event.setIdToNull();
    event.getEventItems().forEach(eventItem -> reverseItem(eventItem));
    return event;
  }

  private void addEventExtId(Event event, Long eventExtId, Integer sequenceNr) {
    event.setEventExtId(eventExtId);
    event.setSequenceNr(sequenceNr);
  }

  //
  /* check approved gsn (3. part of addEvent) */
  private class ApprovedToverGsn {
    BigDecimal tover;
    Long lastGsn;
    BookKeepingDimension dimension;
    BookKeepingItemType itemType;
    Long itemId;
  }

  private void addApprovedToverAndGsn(HashMap<Long, ApprovedToverGsn> approvedBalanceGsnMap,
      EventItem eventItem) {

    // update or add tover
    ApprovedToverGsn approvedToverGsn = approvedBalanceGsnMap.get(eventItem.getTimecurve().getId());
    if (approvedToverGsn != null) {
      approvedToverGsn.tover = new BigDecimal(0)
          .add(approvedToverGsn.tover.add(eventItem.getValue1()));
    } else {
      approvedToverGsn = new ApprovedToverGsn();
      approvedToverGsn.tover = eventItem.getValue1();
      approvedToverGsn.dimension = eventItem.getDimension();
      approvedToverGsn.itemType = eventItem.getItemType();
      approvedToverGsn.itemId = eventItem.getItemId();
    }

    // add to hash map
    approvedBalanceGsnMap.put(eventItem.getTimecurve().getId(), approvedToverGsn);
  }

  private void checkGsn(Long timecurveId, ApprovedToverGsn approvedToverGsn, Long approvedGsn) {
    Long lastGsn = eventItemRepository
        .findQueryLastGsnByTimecurve(timecurveId, approvedToverGsn.dimension,
            approvedToverGsn.itemType, approvedToverGsn.itemId);
//    EventItem foundEventItem = eventItemRepository
//        .findFirstByTimecurveEntityIdAndDimensionAndItemTypeOrderByGsnDescEventEntityIdDesc(timecurveId,
//            approvedToverGsn.dimension, approvedToverGsn.itemType).orElse(new EventItem());
    if (lastGsn != null && approvedGsn < lastGsn) {
      throw EventItemGsnBiggerApproved(lastGsn, approvedGsn);
    }
  }

  private void checkApprovedToverGsn(Event newEvent, Event lastEvent, Long approvedGsn) {

    // if apprvedGsn is null then don't do any checks
    if (approvedGsn == null) {
      return;
    }

    // map per timecurveId -> only one check per timecurveId supported
    HashMap<Long, ApprovedToverGsn> approvedToverGsnMap = new HashMap<>();

    // LAST EVENT
    if (lastEvent != null) {
      lastEvent.getEventItems().stream()
          // check if check on approved balance is needed (on itemType and on timecurve object)
          .filter(
              item -> item.getItemType().buildApprovedBalance()
                  && item.getTimecurve().getNeedBalanceApproval())
          .forEach(e -> {
            addApprovedToverAndGsn(approvedToverGsnMap, e);
          });
    }

    // NEW EVENT
    newEvent.getEventItems().stream()
        // check if check on approved balance is needed (on itemType and on timecurve object)
        .filter(
            item -> item.getItemType().buildApprovedBalance()
                && item.getTimecurve().getNeedBalanceApproval())
        .forEach(e -> {
          addApprovedToverAndGsn(approvedToverGsnMap, e);
        });

    approvedToverGsnMap.entrySet().stream()
        .filter(a -> a.getValue().tover.compareTo(BigDecimal.ZERO) != 0)
        .forEach(f -> checkGsn(f.getKey(), f.getValue(), approvedGsn));
  }


  //
  /* put event (last & new) (4. part of addEvent) */
  private Event putEvent(Event event, Event lastEvent) {
    /*reveres last event if not null*/
    if (lastEvent != null) {
      eventRepository.save(lastEvent);
    }

    /*insert new one with higher sequenceNr*/
    return eventRepository.save(event);
  }

  //
  /* generate message to be published (5. part of addEvent) */
  private void publishItems(EventItem item) {
    String objectId = objectTimecurveRelationService.getObjectByTimecuveIdAndDate(item.getTimecurve().getId(),item.getDate1());
    EventMessage eventMessage = EventMessage.builder()
        .eventExtId(item.getEvent().getEventExtId())
        .eventSequenceNr(item.getEvent().getSequenceNr())
        .orderId(item.getEvent().getOrderId())
        .eventItemRowNr(item.getRowNr())
        .tenantId(item.getTenantId())
        .dimension(item.getDimension())
        .timecurveId(item.getTimecurve().getId())
        .itemType(item.getItemType())
        .itemId(item.getItemId())
        .date1(item.getDate1())
        .date2(item.getDate2())
        .objectId(objectId)
        .value1(item.getValue1())
        .value2(item.getValue2())
        .value3(item.getValue3())
        .tover1(item.getTover1())
        .tover2(item.getTover2())
        .tover3(item.getTover3())
        .status(item.getEvent().getStatus())
        .gsn(item.getGsn()).build();
    this.eventMessaging.sendEvent(eventMessage);
  }

  private void publishEvent(Event event) {
    event.getEventItems().forEach(item -> publishItems(item));
  }

  //
  /* update balance 6. part of addEvent) */
  private Event relvUpdate(Event event) {
    if (event != null && (event.getStatus().equals(EventStatus.APPROVED) || event
        .getStatus().equals(EventStatus.BOOKED))) {
      return event;
    }
    return null;
  }

  private void updateBalance(Event newEvent, Event lastEvent) {
    approvedBalanceService.addEvent(newEvent, lastEvent);
  }

  /* MAIN: Add Event. If existing reverse items and reinsert new ones.
     Additional check clearing consistency and update ApprovedBalance*/
  @Transactional
  public Event addEvent(Event event, Long approvedGsn) {
    log.debug("Approved GSN: " + approvedGsn);
    // 1. evaluate clearing
    evalClearing(event);

    // 2. find EventExtId and if not existing add one
    Event lastEvent = null;
    if (event.getEventExtId() == null) {
      addEventExtId(event, eventRepository.getNextEventExtId(), 1);
    } else {
      lastEvent = getLastEvent(event.getEventExtId());
      log.debug(
          "NEW EVENT: " + lastEvent.getEventExtId() + " : " + (-lastEvent.getSequenceNr() + 1));
      addEventExtId(event, lastEvent.getEventExtId(), -lastEvent.getSequenceNr() + 1);
    }

    // 3. check approved gsn
    checkApprovedToverGsn(relvUpdate(event), relvUpdate(lastEvent), approvedGsn);

    // 4. add event & items
    event = putEvent(event, lastEvent);

    // 5. messaging event -- may replace with outbox table concept
    publishEvent(event);

    //@todo: implement update async in own service
    // 6. check approved gsn for relevant timecurves & update balance
    updateBalance(relvUpdate(event), relvUpdate(lastEvent));

    return event;
  }
}
