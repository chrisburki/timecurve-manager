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
import timecurvemanager.bookkeeping.application.BookingService;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;
import timecurvemanager.gsn.application.GsnService;
import timecurvemanager.position.application.PositionService;
import timecurvemanager.position.domain.api.PositionCommand;
import timecurvemanager.position.domain.api.PositionExternalEvent;
import timecurvemanager.position.domain.model.PositionValueType;

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
  public CommandLineRunner demo(
      BookingService bookingService, PositionService positionService, GsnService gsnService) {
    // Test Data Booking
    final String orderId = "pay_1";
    final BookingStatus status = BookingStatus.APPROVED;
    final String useCase1 = "pay";
    final String useCase2 = "xfer";

    // Test Data BookingItem
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
      // Position 1
      PositionCommand positionCommand1 = PositionCommand.builder()
          .tenantId(tenantId)
          .containerId("DT")
          .valueType(PositionValueType.CURRENCY)
          .valueTag("CHF")
          .build();
      PositionExternalEvent positionExternalEvent1 = positionService
          .processPositionCommand(positionCommand1);
      log.info("Pos1: " + positionExternalEvent1.getId());

      // Position 2
      PositionCommand positionCommand2 = PositionCommand.builder()
          .tenantId(tenantId)
          .containerId("C1")
          .tag("CH1231231231108")
          .valueType(PositionValueType.CURRENCY)
          .valueTag("CHF")
          .build();
      PositionExternalEvent positionExternalEvent2 = positionService
          .processPositionCommand(positionCommand2);
      log.info("Pos2: " + positionExternalEvent2.getId());

      // Position 3
      PositionCommand positionCommand3 = PositionCommand.builder()
          .tenantId(tenantId)
          .containerId("C2")
          .tag("CH1231231231123")
          .valueType(PositionValueType.CURRENCY)
          .valueTag("CHF")
          .build();
      PositionExternalEvent positionExternalEvent3 = positionService
          .processPositionCommand(positionCommand3);
      log.info("Pos3: " + positionExternalEvent3.getId());

      // Position 4
      PositionCommand positionCommand4 = PositionCommand.builder()
          .tenantId(tenantId)
          .containerId("C2")
          .valueType(PositionValueType.SECURITY)
          .valueTag("NESN")
          .build();
      PositionExternalEvent positionExternalEvent4 = positionService
          .processPositionCommand(positionCommand4);
      log.info("Pos4: " + positionExternalEvent4.getId());

      // Booking1
      BookingCommand bookingCommand1 = BookingCommand.builder()
          .orderId(orderId)
          .tenantId(tenantId)
          .dimension(dimension)
          .status(status)
          .useCase(useCase1)
          .date1(date1)
          .date2(date2)
          .gsn(gsnService.getCurrGsn().getId())
          .build();
      bookingCommand1
          .createBookingItem(rowNr, positionExternalEvent2.getId().toString(), itemType, itemId,
              value1, value2,
              value3, tover1, tover2, tover3);
      bookingCommand1
          .createBookingItem(rowNr + 1, positionExternalEvent1.getId().toString(), itemType, itemId,
              value1.negate(), value2, value3, tover1.negate(), tover2,
              tover3);
      bookingService.processBookingCommand(bookingCommand1);

      // Booking2
      BookingCommand bookingCommand2 = BookingCommand.builder()
          .orderId(orderId)
          .tenantId(tenantId)
          .dimension(dimension)
          .status(status)
          .useCase(useCase2)
          .date1(date3)
          .date2(date4)
          .gsn(gsnService.getCurrGsn().getId())
          .build();
      bookingCommand2
          .createBookingItem(rowNr, positionExternalEvent1.getId().toString(), itemType, itemId,
              BigDecimal.ONE.add(value1).negate(), value2,
              value3, BigDecimal.ONE.add(tover1).negate(), tover2, tover3);
      bookingCommand2
          .createBookingItem(rowNr + 1, positionExternalEvent3.getId().toString(), itemType, itemId,
              BigDecimal.ONE.add(value1), value2, value3, BigDecimal.ONE.add(value1), tover2,
              tover3);
      BookingExternalEvent bookingExternalEvent = bookingService
          .processBookingCommand(bookingCommand2);

      // Booking3
      BookingCommand bookingCommand3 = BookingCommand.builder()
          .extId(bookingExternalEvent.getBookingExtId())
          .orderId(orderId)
          .tenantId(tenantId)
          .dimension(dimension)
          .status(status)
          .useCase(useCase2)
          .date1(date3)
          .date2(date4)
          .gsn(gsnService.getCurrGsn().getId())
          .build();
      bookingCommand3
          .createBookingItem(rowNr, positionExternalEvent1.getId().toString(), itemType, itemId,
              BigDecimal.TEN.add(value1).negate(), value2,
              value3, tover1.negate(), tover2, tover3);
      bookingCommand3
          .createBookingItem(rowNr + 1, positionExternalEvent3.getId().toString(), itemType, itemId,
              BigDecimal.TEN.add(value1), value2, value3, tover1, tover2,
              tover3);
      bookingService.processBookingCommand(bookingCommand3);

      log.info("Active Profile: " + activeProfile);
      log.info("GSN: " + LocalDateTime.now()
          .getLong(ChronoField.EPOCH_DAY) + " sec: " + LocalTime.now()
          .getLong(ChronoField.SECOND_OF_DAY));
      log.info("GSN: " + gsnService.getCurrGsn().getId());
    };

  }
}
