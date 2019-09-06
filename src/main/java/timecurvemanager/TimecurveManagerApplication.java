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
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingDimension;
import timecurvemanager.bookkeeping.domain.booking.model.BookKeepingItemType;
import timecurvemanager.bookkeeping.domain.booking.model.Booking;
import timecurvemanager.bookkeeping.domain.booking.model.BookingItem;
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
      TimecurveService timecurveService, BookingService bookingService,
      PositionService positionService, GsnService gsnService) {
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
      Timecurve timecurve0 = timecurveService
          .createTimecurve(position0.getId().toString(), position0.getTenantId(), "pos",
              position0.getClearingReference(), position0.getNeedBalanceApproval(), date3);
      // Position 1
      Position position1 = new Position(null, tenantId, "C1", "C1#CHF#MACC",
          "CHF Money Account for Container 1", PositionValueType.CURRENCY, "CHF", true);
      position1 = positionService.addPosition(position1);
      Timecurve timecurve1 = timecurveService
          .createTimecurve(position1.getId().toString(), position1.getTenantId(), "pos",
              position1.getClearingReference(), position1.getNeedBalanceApproval(), date3);

      // Position 2
      Position position2 = new Position(null, tenantId, "C2", "C2#CHF#MACC",
          "CHF Money Account for Container 2", PositionValueType.CURRENCY, "CHF", true);
      position2 = positionService.addPosition(position2);
      Timecurve timecurve2 = timecurveService
          .createTimecurve(position2.getId().toString(), position2.getTenantId(), "pos",
              position2.getClearingReference(), position2.getNeedBalanceApproval(), date3);

      // fetch all timecurves
      log.info("Timecurves found with findAll():");
      log.info("-------------------------------");
      for (Timecurve timecurve : timecurveService.listObjects()) {
        log.info(timecurve.toString());
      }

      // Booking1
      Booking booking1 = new Booking(null, orderId, tenantId, dimension, status, useCase1, date1,
          date2);

      BookingItem bookingItem11 = new BookingItem(rowNr, timecurve1.getId(),
          itemType, itemId, value1, value2, value3, tover1, tover2, tover3, null
      );

      booking1.addBookingItem(bookingItem11);

      BookingItem bookingItem12 = new BookingItem(rowNr + 1,
          timecurve0.getId(),
          itemType, itemId, value1.negate(), value2, value3, tover1.negate(), tover2,
          tover3, null);
      booking1.addBookingItem(bookingItem12);
      booking1 = bookingService.addBooking(booking1, null);
      log.info("booking id: " + booking1.getId() + " bookingExtId: " + booking1.getBookingExtId());

      // Booking2
      Booking booking2 = new Booking(null, orderId, tenantId, dimension, status, useCase2, date3,
          date4);

      BookingItem bookingItem21 = new BookingItem(rowNr,
          timecurve0.getId(), itemType, itemId, BigDecimal.ONE.add(value1).negate(),
          value2, value3, BigDecimal.ONE.add(tover1).negate(), tover2, tover3, null);
      booking2.addBookingItem(bookingItem21);

      BookingItem bookingItem22 = new BookingItem(rowNr + 1,
          timecurve2.getId(), itemType, itemId, BigDecimal.ONE.add(value1), value2,
          value3, BigDecimal.ONE.add(tover1), tover2, tover3, null);
      booking2.addBookingItem(bookingItem22);
      booking2 = bookingService.addBooking(booking2, null);
      log.info("booking id: " + booking2.getId() + " bookingExtId: " + booking2.getBookingExtId());
      //TimeUnit.SECONDS.sleep(1);
      // Booking3
      Booking booking3 = new Booking(booking2.getBookingExtId(), orderId, tenantId, dimension,
          status,
          useCase2, date3, date4);

      BookingItem bookingItem31 = new BookingItem(rowNr,
          timecurve0.getId(), itemType, itemId, BigDecimal.TEN.add(value1).negate(),
          value2, value3, tover1.negate(), tover2, tover3, null);
      booking3.addBookingItem(bookingItem31);

      BookingItem bookingItem32 = new BookingItem(rowNr + 1,
          timecurve2.getId(), itemType, itemId, BigDecimal.TEN.add(value1), value2,
          value3, tover1, tover2, tover3, null);
      booking3.addBookingItem(bookingItem32);
      booking3 = bookingService.addBooking(booking3, null);
      log.info("booking id: " + booking3.getId() + " bookingExtId: " + booking3.getBookingExtId());

      log.info("Active Profile: " + activeProfile);
      log.info("GSN: " + LocalDateTime.now()
          .getLong(ChronoField.EPOCH_DAY) + " sec: " + LocalTime.now()
          .getLong(ChronoField.SECOND_OF_DAY));
      log.info("GSN: " + gsnService.getCurrGsn().getId());
    };

  }
}
