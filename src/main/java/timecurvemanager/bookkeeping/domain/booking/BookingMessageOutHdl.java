package timecurvemanager.bookkeeping.domain.booking;

import timecurvemanager.bookkeeping.domain.booking.api.BookingDomainEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

public interface BookingMessageOutHdl {

  /*Publish Domain Event to message broker*/
  void sendDomainEvent(BookingDomainEvent event);

  /*Publish External Event to message broker*/
  void sendExternalEvent(BookingExternalEvent event);

  /*Send Reply for a Command (if requested) (in the header send the CorrelationId (received MessageId)*/
  void sendReply(BookingExternalEvent event, String replyTopic, String correlationId);

  //Publish Command --> for paymentOrder service (in the header send MessageId (e.g. orderId) and the ReturnAddress (e.g. ReplyChannel)

}
