package timecurvemanager.bookkeeping.domain.booking;

import org.springframework.messaging.MessageHeaders;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;

public interface BookingCommandHdl {

  /*Receive command Message*/
  void receiveCommand(timecurvemanager.bookkeeping.domain.booking.api.BookingCommand event, MessageHeaders headers);

}
