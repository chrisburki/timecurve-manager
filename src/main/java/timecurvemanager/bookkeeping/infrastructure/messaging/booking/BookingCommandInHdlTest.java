package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.BookingCommandInHdl;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;

@Component
@Slf4j
@Profile("test")
public class BookingCommandInHdlTest implements BookingCommandInHdl {

  @Override
  public void receiveCommand(BookingCommand event, MessageHeaders headers) {
    log.info("Receive Test Booking Command");
  }
}
