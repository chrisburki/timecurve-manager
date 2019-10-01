package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.BookingMessageOutHdl;
import timecurvemanager.bookkeeping.domain.booking.api.BookingDomainEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

@Component
@Slf4j
@Profile("prod")
public class BookingMessageOutHdlProd implements BookingMessageOutHdl {

  @Override
  public void sendDomainEvent(BookingDomainEvent event) {
    log.info("Publish Prod Booking Domian Event");
  }

  @Override
  public void sendExternalEvent(BookingExternalEvent event) {
    log.info("Publish Prod Booking External Event");
  }

  @Override
  public void sendReply(BookingExternalEvent event) {
    log.info("Publish Prod Booking Reply");
  }


}