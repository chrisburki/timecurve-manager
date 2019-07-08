package timecurvemanager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import timecurvemanager.application.EventService;
import timecurvemanager.application.PositionService;
import timecurvemanager.application.TimecurveObjectService;
import timecurvemanager.domain.event.Event;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItem;
import timecurvemanager.domain.event.EventItemType;
import timecurvemanager.domain.event.EventStatus;
import timecurvemanager.domain.position.Position;
import timecurvemanager.domain.position.PositionValueType;
import timecurvemanager.domain.timecurveobject.TimecurveObject;

//@todo: add position tcObject history table

@SpringBootApplication
@Slf4j
public class TimecurveManagerApplication {

  // private static final Logger log = LoggerFactory.getLogger(TimecurveManagerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(TimecurveManagerApplication.class);
  }


  @Bean
  public CommandLineRunner demo(TimecurveObjectService timecurveObjectService,
      EventService eventService, PositionService positionService) {
    // Test Data Event
    final EventStatus status = EventStatus.OPEN;
    final String useCase1 = "pay";
    final String useCase2 = "xfer";
    final Integer seqNr = 1;

    // Test Data TimecurveObject
    final String name1 = "Object 1";
    final String name2 = "Object 2";
    final String clearingRef = "CHF";
    final Boolean needBalanceApproval = true;

    // Test Data EventItem
    final Integer rowNr = 1;
    final String tenantId = "AAA";
    final EventDimension dimension = EventDimension.SUBLEDGER;
    final EventItemType itemType = EventItemType.BASIC;
    final Long itemId = 1L;
    final LocalDate date1 = LocalDate.now();
    final LocalDate date2 = LocalDate.now();
    final LocalDate date3 = LocalDate.MAX;
    final LocalDate date4 = LocalDate.now().minus(Period.ofDays(100));
    final BigDecimal value1 = new BigDecimal(1000);
    final BigDecimal value2 = null;
    final BigDecimal value3 = null;
    final BigDecimal tover1 = new BigDecimal(1000);
    final BigDecimal tover2 = null;
    final BigDecimal tover3 = null;

    return (args) -> {
      positionService.addPosition(
          new Position(null, tenantId, "1C", "1#CHF#MACC", "CHF Money Account for Container 1",
              PositionValueType.CURRENCY, "CHF"));

      positionService.addPosition(
          new Position(null, tenantId, "2C", "2#CHF#MACC", "CHF Money Account for Container 2",
              PositionValueType.CURRENCY, "CHF"));

      // save a couple of timecurves
      timecurveObjectService
          .addTimecurve(new TimecurveObject(null, tenantId, "Share 1 CHF", null, false));
      timecurveObjectService
          .addTimecurve(new TimecurveObject(null, tenantId, "Share 2 EUR", null, false));
      timecurveObjectService
          .addTimecurve(new TimecurveObject(null, tenantId, "Bond 1 CHF", null, false));
      timecurveObjectService
          .addTimecurve(
              new TimecurveObject(null, tenantId, "Money Account 1 CHF", clearingRef, true));
      timecurveObjectService
          .addTimecurve(
              new TimecurveObject(null, tenantId, "Money Account 2 CHF", clearingRef, true));

      // fetch all timecurves
      log.info("Timecurves found with findAll():");
      log.info("-------------------------------");
      for (TimecurveObject timecurveObject : timecurveObjectService.listObjects()) {
        log.info(timecurveObject.toString());
      }
      log.info("");

      // fetch an individual TimecurveObject by ID
      log.info("TimecurveObject found with findById(1L):");
      log.info("--------------------------------");
//      log.info(timecurveObjectService.getById(1L).toString());
      log.info("");

      TimecurveObject timecurveObject1 = timecurveObjectService
          .addTimecurve(
              new TimecurveObject(null, tenantId, name1, clearingRef, needBalanceApproval));
      log.info("object id: " + timecurveObject1.getId());

      TimecurveObject timecurveObject2 = timecurveObjectService
          .addTimecurve(
              new TimecurveObject(null, tenantId, name2, clearingRef, !needBalanceApproval));
      log.info("object id: " + timecurveObject1.getId());

      // Event1
      Event event = new Event(null, null, seqNr, tenantId, dimension, status,
          useCase1, date1, date2);

      EventItem eventItem1 = new EventItem(null, null, rowNr, tenantId, dimension,
          timecurveObject1, itemType, itemId,
          date1, date2, value1, value2, value3, tover1, tover2, tover3, null);

      event.addEventItem(eventItem1);

      EventItem eventItem2 = new EventItem(null, null, rowNr + 1, tenantId, dimension,
          timecurveObject2, itemType, itemId,
          date1, date2, value1.negate(), value2, value3, tover1.negate(), tover2, tover3, null);
      event.addEventItem(eventItem2);
      log.info("eventItem2 eventextid: " + eventItem2.getEvent().getEventExtId());
      event = eventService.addEvent(event);
      log.info("event id: " + event.getId());

      // Event1
      Event event2 = new Event(null, null, seqNr, tenantId, dimension, status,
          useCase2, date3, date4);

      EventItem eventItem21 = new EventItem(null, null, rowNr, tenantId, dimension,
          timecurveObject2, itemType, itemId, date3, date4, value1.negate(), value2, value3,
          tover1.negate(), tover2, tover3, null);
      event.addEventItem(eventItem21);

      EventItem eventItem22 = new EventItem(null, null, rowNr + 1, tenantId, dimension,
          timecurveObject1, itemType, itemId, date3, date4, value1, value2, value3, tover1, tover2,
          tover3, null);
      event.addEventItem(eventItem22);
      log.info("eventItem2 eventextid: " + eventItem2.getEvent().getEventExtId());
      event = eventService.addEvent(event2);
      log.info("event id: " + event.getId() + " eventExtId: " + event.getEventExtId());
    };
  }
}
