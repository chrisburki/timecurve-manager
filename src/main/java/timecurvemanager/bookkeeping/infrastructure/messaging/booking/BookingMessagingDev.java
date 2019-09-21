package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.BookingMessaging;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingDomainEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

@Component
@Slf4j
@Profile("dev")
public class BookingMessagingDev implements BookingMessaging {

  @Override
  public void sendDomainEvent(BookingDomainEvent event) {
    log.info("Publish Dev Booking Domian Event");
  }

  @Override
  public void sendExternalEvent(BookingExternalEvent event) {
    log.info("Publish Dev Booking External Event");
  }

  @Override
  public void sendReply(BookingExternalEvent event) {
    log.info("Publish Dev Booking Reply");
  }

}
