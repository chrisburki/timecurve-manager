package timecurvemanager.bookkeeping.domain.booking;

import org.springframework.messaging.MessageHeaders;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingDomainEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

public interface BookingMessaging {

  /*Publish Domain Event to message broker*/
  void sendDomainEvent(BookingDomainEvent event);

  /*Receive command Message*/
  void receiveCommand(BookingCommand event, MessageHeaders headers);

  /*Publish External Event to message broker*/
  void sendExternalEvent(BookingExternalEvent event);

  /*Send Reply for a Command (if requested) (in the header send the CorrelationId (received MessageId)*/
  void sendReply(BookingExternalEvent event);

  //Publish Command --> for paymentOrder service (in the header send MessageId (e.g. orderId) and the ReturnAddress (e.g. ReplyChannel)

}
