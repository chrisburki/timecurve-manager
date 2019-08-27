package timecurvemanager.domain.event;

import timecurvemanager.domain.event.messaging.BookingMessage;
import timecurvemanager.domain.event.messaging.BookingDomainEventMessage;
import timecurvemanager.domain.event.messaging.EventReplyMessage;

public interface EventMessaging {

  /*Publish Domain Event to message broker*/
  void sendDomainEvent(BookingDomainEventMessage domainEventMessage);

  /*Receive command Message*/
  void receiveCommand(BookingMessage commandMessage);

  /*Publish reply Message (in the header send the CorrelationId (received MessageId)*/
  void sendReply(EventReplyMessage replyMessage);


  //Publish Command --> for paymentOrder service (in the header send MessageId (e.g. orderId) and the ReturnAddress (e.g. ReplyChannel)
  //void sendCommand
}
