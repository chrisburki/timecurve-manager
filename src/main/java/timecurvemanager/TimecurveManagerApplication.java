package timecurvemanager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
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

@SpringBootApplication
@Slf4j
public class TimecurveManagerApplication {

  // private static final Logger log = LoggerFactory.getLogger(TimecurveManagerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(TimecurveManagerApplication.class);
  }

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Bean
  public CommandLineRunner demo(TimecurveObjectService timecurveObjectService,
      EventService eventService, PositionService positionService) {
    // Test Data Event
    final String orderId = "pay_1";
    final EventStatus status = EventStatus.APPROVED;
    final String useCase1 = "pay";
    final String useCase2 = "xfer";

    // Test Data EventItem
    final Integer rowNr = 1;
    final String tenantId = "AAA";
    final EventDimension dimension = EventDimension.SUBLEDGER;
    final EventItemType itemType = EventItemType.BASIC;
    final Long itemId = 1L;
    final LocalDate date1 = LocalDate.now();
    final LocalDate date2 = LocalDate.now();
    final LocalDate date3 = LocalDate.of(2000, 01, 01);
    final LocalDate date4 = LocalDate.now().minus(Period.ofDays(100));
    final BigDecimal value1 = new BigDecimal(1000);
    final BigDecimal value2 = null;
    final BigDecimal value3 = null;
    final BigDecimal tover1 = value1;
    final BigDecimal tover2 = null;
    final BigDecimal tover3 = null;

    return (args) -> {
      // Position 0
      Position position0 = new Position(null, tenantId, "DT", "DT#CHF#INT",
          "CHF Money Account for Container 1", PositionValueType.CURRENCY, "CHF", false);
      position0 = positionService.addPosition(position0);
      TimecurveObject timecurveObject0 = positionService
          .addTimecurveForPositionAndDate(position0, LocalDate.of(2000, 1, 1));

      // Position 1
      Position position1 = new Position(null, tenantId, "C1", "C1#CHF#MACC",
          "CHF Money Account for Container 1", PositionValueType.CURRENCY, "CHF", true);
      position1 = positionService.addPosition(position1);
      TimecurveObject timecurveObject1 = positionService
          .addTimecurveForPositionAndDate(position1, LocalDate.of(2000, 4, 8));

      // Position 2
      Position position2 = new Position(null, tenantId, "C2", "C2#CHF#MACC",
          "CHF Money Account for Container 2", PositionValueType.CURRENCY, "CHF", true);
      position2 = positionService.addPosition(position2);
      TimecurveObject timecurveObject2 = positionService
          .addTimecurveForPositionAndDate(position2, LocalDate.of(2003, 3, 23));

      // fetch all timecurves
      log.info("Timecurves found with findAll():");
      log.info("-------------------------------");
      for (TimecurveObject timecurveObject : timecurveObjectService.listObjects()) {
        log.info(timecurveObject.toString());
      }

      // Event1
      Event event1 = new Event(null, orderId, tenantId, dimension, status, useCase1, date1, date2);

      EventItem eventItem11 = new EventItem(null, rowNr, tenantId, dimension, timecurveObject1,
          itemType, itemId, date1, date2, value1, value2, value3, tover1, tover2, tover3, null);

      event1.addEventItem(eventItem11);

      EventItem eventItem12 = new EventItem(null, rowNr + 1, tenantId, dimension, timecurveObject0,
          itemType, itemId, date1, date2, value1.negate(), value2, value3, tover1.negate(), tover2,
          tover3, null);
      event1.addEventItem(eventItem12);
      event1 = eventService.addEvent(event1);
      log.info("event id: " + event1.getId() + " eventExtId: " + event1.getEventExtId());

      // Event2
      Event event2 = new Event(null, orderId, tenantId, dimension, status, useCase2, date3, date4);

      EventItem eventItem21 = new EventItem(null, rowNr, tenantId, dimension,
          timecurveObject0, itemType, itemId, date3, date4, BigDecimal.ONE.add(value1).negate(),
          value2, value3, BigDecimal.ONE.add(tover1).negate(), tover2, tover3, null);
      event2.addEventItem(eventItem21);

      EventItem eventItem22 = new EventItem(null, rowNr + 1, tenantId, dimension,
          timecurveObject2, itemType, itemId, date3, date4, BigDecimal.ONE.add(value1), value2,
          value3, BigDecimal.ONE.add(tover1), tover2, tover3, null);
      event2.addEventItem(eventItem22);
      event2 = eventService.addEvent(event2);
      log.info("event id: " + event2.getId() + " eventExtId: " + event2.getEventExtId());

      // Event3
      Event event3 = new Event(event2.getEventExtId(), orderId, tenantId, dimension, status,
          useCase2, date3, date4);

      EventItem eventItem31 = new EventItem(null, rowNr, tenantId, dimension,
          timecurveObject0, itemType, itemId, date3, date4, BigDecimal.TEN.add(value1).negate(),
          value2, value3, tover1.negate(), tover2, tover3, null);
      event3.addEventItem(eventItem31);

      EventItem eventItem32 = new EventItem(null, rowNr + 1, tenantId, dimension,
          timecurveObject2, itemType, itemId, date3, date4, BigDecimal.TEN.add(value1), value2,
          value3, tover1, tover2, tover3, null);
      event3.addEventItem(eventItem32);
      event3 = eventService.addEvent(event3);
      log.info("event id: " + event3.getId() + " eventExtId: " + event3.getEventExtId());

      log.info("Active Profile: " + activeProfile);
    };

  }
}
