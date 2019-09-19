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
import timecurvemanager.bookkeeping.application.TimecurveService;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.BookingStatus;
import timecurvemanager.bookkeeping.domain.timecurve.Timecurve;
import timecurvemanager.gsn.application.GsnService;
import timecurvemanager.position.application.PositionService;
import timecurvemanager.position.domain.Position;
import timecurvemanager.position.domain.PositionValueType;

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
      // Position 0
      Position position0 = new Position(null, tenantId, "DT", "DT#CHF#INT",
          "CHF Money Account for Container 1", PositionValueType.CURRENCY, "CHF", false);
      position0 = positionService.addPosition(position0);

      // Position 1
      Position position1 = new Position(null, tenantId, "C1", "C1#CHF#MACC",
          "CHF Money Account for Container 1", PositionValueType.CURRENCY, "CHF", true);
      position1 = positionService.addPosition(position1);

      // Position 2
      Position position2 = new Position(null, tenantId, "C2", "C2#CHF#MACC",
          "CHF Money Account for Container 2", PositionValueType.CURRENCY, "CHF", true);
      position2 = positionService.addPosition(position2);

      // Booking1
      BookingCommand bookingCommand1 = BookingCommand.builder()
          .orderId(orderId)
          .tenantId(tenantId)
          .dimension(dimension)
          .status(status)
          .useCase(useCase1)
          .date1(date1)
          .date2(date2).build();
      bookingCommand1
          .createBookingItem(rowNr, position1.getId().toString(), itemType, itemId, value1, value2,
              value3, tover1, tover2, tover3);
      bookingCommand1
          .createBookingItem(rowNr + 1, position0.getId().toString(), itemType, itemId,
              value1.negate(), value2, value3, tover1.negate(), tover2,
              tover3);
      bookingService.processBookingCommand(bookingCommand1, false);

      // Booking2
      BookingCommand bookingCommand2 = BookingCommand.builder()
          .orderId(orderId)
          .tenantId(tenantId)
          .dimension(dimension)
          .status(status)
          .useCase(useCase2)
          .date1(date3)
          .date2(date4).build();
      bookingCommand2
          .createBookingItem(rowNr, position0.getId().toString(), itemType, itemId,
              BigDecimal.ONE.add(value1).negate(), value2,
              value3, BigDecimal.ONE.add(tover1).negate(), tover2, tover3);
      bookingCommand2
          .createBookingItem(rowNr + 1, position2.getId().toString(), itemType, itemId,
              BigDecimal.ONE.add(value1), value2, value3, BigDecimal.ONE.add(value1), tover2,
              tover3);
      BookingExternalEvent bookingExternalEvent = bookingService
          .processBookingCommand(bookingCommand2, true);

      // Booking3
      BookingCommand bookingCommand3 = BookingCommand.builder()
          .extId(bookingExternalEvent.getBookingExtId())
          .orderId(orderId)
          .tenantId(tenantId)
          .dimension(dimension)
          .status(status)
          .useCase(useCase2)
          .date1(date3)
          .date2(date4).build();
      bookingCommand3
          .createBookingItem(rowNr, position0.getId().toString(), itemType, itemId,
              BigDecimal.TEN.add(value1).negate(), value2,
              value3, tover1.negate(), tover2, tover3);
      bookingCommand3
          .createBookingItem(rowNr + 1, position2.getId().toString(), itemType, itemId,
              BigDecimal.TEN.add(value1), value2, value3, tover1, tover2,
              tover3);
      bookingService.processBookingCommand(bookingCommand3, false);

      log.info("Active Profile: " + activeProfile);
      log.info("GSN: " + LocalDateTime.now()
          .getLong(ChronoField.EPOCH_DAY) + " sec: " + LocalTime.now()
          .getLong(ChronoField.SECOND_OF_DAY));
      log.info("GSN: " + gsnService.getCurrGsn().getId());
    };

  }
}
