package timecurvemanager.bookkeeping.domain.booking;

import org.springframework.messaging.MessageHeaders;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;

public interface BookingCommandInHdl {

  /*Receive command Message*/
  void receiveCommand(BookingCommand event, MessageHeaders headers);

}
