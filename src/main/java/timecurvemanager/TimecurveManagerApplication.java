package timecurvemanager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoField;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import timecurvemanager.application.EventService;
import timecurvemanager.application.GsnService;
import timecurvemanager.application.ObjectTimecurveRelationService;
import timecurvemanager.application.PositionService;
import timecurvemanager.application.TimecurveService;
import timecurvemanager.domain.event.model.BookKeepingDimension;
import timecurvemanager.domain.event.model.Event;
import timecurvemanager.domain.event.model.BookKeepingItemType;
import timecurvemanager.domain.event.model.EventItem;
import timecurvemanager.domain.event.model.EventStatus;
import timecurvemanager.domain.position.Position;
import timecurvemanager.domain.position.PositionValueType;
import timecurvemanager.domain.timecurve.Timecurve;

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
  public CommandLineRunner demo(ObjectTimecurveRelationService relationService,
      TimecurveService timecurveService, EventService eventService,
      PositionService positionService, GsnService gsnService) {
    // Test Data Event
    final String orderId = "pay_1";
    final EventStatus status = EventStatus.APPROVED;
    final String useCase1 = "pay";
    final String useCase2 = "xfer";

    // Test Data EventItem
    final Integer rowNr = 1;
    final String tenantId = "AAA";
    final BookKeepingDimension dimension = BookKeepingDimension.SUBLEDGER;
    final BookKeepingItemType itemType = BookKeepingItemType.BASIC;
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
      Timecurve timecurve0 = new Timecurve(null, tenantId, "pos", position0.getClearingReference(),
          position0.getDoBalanceCheck());
      timecurve0 = relationService.createTimecurve(position0.getId().toString(), timecurve0, date3);
      // Position 1
      Position position1 = new Position(null, tenantId, "C1", "C1#CHF#MACC",
          "CHF Money Account for Container 1", PositionValueType.CURRENCY, "CHF", true);
      position1 = positionService.addPosition(position1);
      Timecurve timecurve1 = new Timecurve(null, tenantId, "pos", position1.getClearingReference(),
          position1.getDoBalanceCheck());
      timecurve1 = relationService.createTimecurve(position1.getId().toString(), timecurve1, date3);

      // Position 2
      Position position2 = new Position(null, tenantId, "C2", "C2#CHF#MACC",
          "CHF Money Account for Container 2", PositionValueType.CURRENCY, "CHF", true);
      position2 = positionService.addPosition(position2);
      Timecurve timecurve2 = new Timecurve(null, tenantId, "pos", position2.getClearingReference(),
          position2.getDoBalanceCheck());
      timecurve2 = relationService.createTimecurve(position2.getId().toString(), timecurve2, date3);

      // fetch all timecurves
      log.info("Timecurves found with findAll():");
      log.info("-------------------------------");
      for (Timecurve timecurve : timecurveService.listObjects()) {
        log.info(timecurve.toString());
      }

      // Event1
      Event event1 = new Event(null, orderId, tenantId, dimension, status, useCase1, date1, date2);

      EventItem eventItem11 = new EventItem(null, rowNr, tenantId, dimension, timecurve1.getId(),
          itemType, itemId, date1, date2, value1, value2, value3, tover1, tover2, tover3, null
      );

      event1.addEventItem(eventItem11);

      EventItem eventItem12 = new EventItem(null, rowNr + 1, tenantId, dimension,
          timecurve0.getId(),
          itemType, itemId, date1, date2, value1.negate(), value2, value3, tover1.negate(), tover2,
          tover3, null);
      event1.addEventItem(eventItem12);
      event1 = eventService.addEvent(event1, null);
      log.info("event id: " + event1.getId() + " eventExtId: " + event1.getEventExtId());

      // Event2
      Event event2 = new Event(null, orderId, tenantId, dimension, status, useCase2, date3, date4);

      EventItem eventItem21 = new EventItem(null, rowNr, tenantId, dimension,
          timecurve0.getId(), itemType, itemId, date3, date4, BigDecimal.ONE.add(value1).negate(),
          value2, value3, BigDecimal.ONE.add(tover1).negate(), tover2, tover3, null);
      event2.addEventItem(eventItem21);

      EventItem eventItem22 = new EventItem(null, rowNr + 1, tenantId, dimension,
          timecurve2.getId(), itemType, itemId, date3, date4, BigDecimal.ONE.add(value1), value2,
          value3, BigDecimal.ONE.add(tover1), tover2, tover3, null);
      event2.addEventItem(eventItem22);
      event2 = eventService.addEvent(event2, null);
      log.info("event id: " + event2.getId() + " eventExtId: " + event2.getEventExtId());
      //TimeUnit.SECONDS.sleep(1);
      // Event3
      Event event3 = new Event(event2.getEventExtId(), orderId, tenantId, dimension, status,
          useCase2, date3, date4);

      EventItem eventItem31 = new EventItem(null, rowNr, tenantId, dimension,
          timecurve0.getId(), itemType, itemId, date3, date4, BigDecimal.TEN.add(value1).negate(),
          value2, value3, tover1.negate(), tover2, tover3, null);
      event3.addEventItem(eventItem31);

      EventItem eventItem32 = new EventItem(null, rowNr + 1, tenantId, dimension,
          timecurve2.getId(), itemType, itemId, date3, date4, BigDecimal.TEN.add(value1), value2,
          value3, tover1, tover2, tover3, null);
      event3.addEventItem(eventItem32);
      event3 = eventService.addEvent(event3, null);
      log.info("event id: " + event3.getId() + " eventExtId: " + event3.getEventExtId());

      log.info("Active Profile: " + activeProfile);
      log.info("GSN: " + LocalDateTime.now()
          .getLong(ChronoField.EPOCH_DAY) + " sec: " + LocalTime.now()
          .getLong(ChronoField.SECOND_OF_DAY));
      log.info("GSN: " + gsnService.getCurrGsn().getId());
    };

  }
}
