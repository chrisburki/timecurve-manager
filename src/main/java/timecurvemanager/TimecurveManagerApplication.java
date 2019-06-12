package timecurvemanager;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import timecurvemanager.domain.event.EventDimension;
import timecurvemanager.domain.event.EventItemType;
import timecurvemanager.domain.event.EventStatus;
import timecurvemanager.domain.timecurveobject.TimecurveObject;
import timecurvemanager.domain.timecurveobject.TimecurveObjectRepository;
import timecurvemanager.domain.timecurveobject.TimecurveObjectValueType;
import timecurvemanager.infrastructure.persistence.event.EventEntity;
import timecurvemanager.infrastructure.persistence.event.EventEntityRepository;
import timecurvemanager.infrastructure.persistence.event.EventItemEntity;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntity;
import timecurvemanager.infrastructure.persistence.timecurveobject.TimecurveObjectEntityRepository;

@SpringBootApplication
public class TimecurveManagerApplication {

  private static final Logger log = LoggerFactory.getLogger(TimecurveManagerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(TimecurveManagerApplication.class);
  }


  @Bean
  public CommandLineRunner demo(TimecurveObjectRepository timecurveRepository,
      TimecurveObjectEntityRepository timecurveObjectEntityRepository,
      EventEntityRepository eventRepository) {
    // Test Data Event
    final EventStatus status = EventStatus.OPEN;
    final Long eventExtId = 1L;
    final String useCase = "pay";
    final Integer seqNr = 1;

    // Test Data TimecurveObject
    final String tag = "TAG1";
    final String name = "Object 1";
    final TimecurveObjectValueType objectValueType = TimecurveObjectValueType.CURRENCY;
    final String valueTag = "CHF";
    final String clearingRef = "CHF";
    final Boolean needBalanceApproval = true;

    // Test Data EventItem
    final Integer rowNr = 1;
    final String tenantId = "AAA";
    final EventDimension dimension = EventDimension.SUBLEDGER;
    final Long timecurveId = 1L;
    final EventItemType itemType = EventItemType.BASIC;
    final Long itemId = 1L;
    final LocalDate date1 = LocalDate.now();
    final LocalDate date2 = LocalDate.now();
    final BigDecimal value1 = new BigDecimal(1000);
    final BigDecimal value2 = null;
    final BigDecimal value3 = null;
    final BigDecimal tover1 = new BigDecimal(100);
    final BigDecimal tover2 = null;
    final BigDecimal tover3 = null;

    return (args) -> {
      // save a couple of timecurves

      timecurveRepository.save(new TimecurveObject(null, tenantId, "share1chf", "Share 1 CHF",
          TimecurveObjectValueType.SECURITY, valueTag, null, false));
      timecurveRepository.save(new TimecurveObject(null, tenantId, "share2eur", "Share 2 EUR",
          TimecurveObjectValueType.SECURITY, valueTag, null, false));
      timecurveRepository.save(new TimecurveObject(null, tenantId, "bond1chf", "Bond 1 CHF",
          TimecurveObjectValueType.CURRENCY, valueTag, null, true));
      timecurveRepository
          .save(new TimecurveObject(null, tenantId, "macc1eur", "Money Account 1 CHF",
              TimecurveObjectValueType.CURRENCY, valueTag, clearingRef, true));
      timecurveRepository
          .save(new TimecurveObject(null, tenantId, "macc2chf", "Money Account 2 CHF",
              TimecurveObjectValueType.CURRENCY, valueTag, clearingRef, true));

      // fetch all timecurves
      log.info("Timecurves found with findAll():");
      log.info("-------------------------------");
      for (TimecurveObject timecurveObject : timecurveRepository.findAll()) {
        log.info(timecurveObject.toString());
      }
      log.info("");

      // fetch an individual TimecurveObject by ID
      timecurveRepository.findById(1L)
          .ifPresent(timecurve -> {
            log.info("TimecurveObject found with findById(1L):");
            log.info("--------------------------------");
            log.info(timecurve.toString());
            log.info("");
          });

      // fetch timecurves by last name
      log.info("TimecurveObject found with findByName('Share 1 CHF'):");
      log.info("--------------------------------------------");
      timecurveRepository.findByName("Share 1 CHF").forEach(bauer -> {
        log.info(bauer.toString());
      });
      timecurveRepository.findByTag("macc1eur").get().toString();
      // for (TimecurveObject macc1 : repository.findByLastName("macc1eur")) {
      // log.info(macc1.toString());
      // }
      log.info("");

      TimecurveObjectEntity timecurveObject = new TimecurveObjectEntity(tenantId, tag, name,
          objectValueType,
          valueTag, clearingRef, needBalanceApproval);
      log.info("object before: " + timecurveObject.getId());

      timecurveObjectEntityRepository.save(timecurveObject);
      log.info("object after: " + timecurveObject.getId());

      EventEntity event = new EventEntity(eventExtId, seqNr, tenantId, dimension, status,
          useCase,
          date1, date2);
      EventItemEntity eventItem = new EventItemEntity(rowNr, tenantId, dimension,
          timecurveObject, itemType, itemId,
          date1, date2, value1, value2, value3, tover1, tover2, tover3);
      log.info("event before: " + event.getId());
      event.addEventItem(eventItem);
      log.info("event afte add item: " + event.getId());
      eventRepository.save(event);
      log.info("event after: " + event.getId());

    };
  }


}
