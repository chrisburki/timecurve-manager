package timecurvemanager.infrastructure.messaging.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.messaging.BookingMessage;
import timecurvemanager.domain.event.messaging.BookingDomainEventMessage;
import timecurvemanager.domain.event.messaging.EventReplyMessage;

@Service
@Slf4j
@Profile("int")
public class EventMessagingIntImpl implements timecurvemanager.domain.event.EventMessaging {

  private final KafkaTemplate<String, BookingDomainEventMessage> kafkaTemplate;

  @Value(value = "${spring.kafka.topic.domain-event-booking}")
  private String domainEventBookingTopic;

  @Value(value = "${spring.kafka.topic.reply-booking}")
  private String ReplyBookingTopic;

  public EventMessagingIntImpl(
      KafkaTemplate<String, BookingDomainEventMessage> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  //https://www.programcreek.com/java-api-examples/?code=langtianya/spring4-understanding/spring4-understanding-master/spring-messaging/src/main/java/org/springframework/messaging/simp/broker/SimpleBrokerMessageHandler.java

  @Override
  public void sendDomainEvent(BookingDomainEventMessage domainEventMessage) {
    log.info("Publish Domain Event Booking");
    Message<BookingDomainEventMessage> message = MessageBuilder
        .withPayload(domainEventMessage)
        .setHeader(KafkaHeaders.TOPIC, domainEventBookingTopic)
        .build();
    kafkaTemplate.send(message);
  }

  @KafkaListener(topics = "${spring.kafka.topic.command-booking}")
  //, groupId = "foo", containerFactory = "eventContainerFactory")
  @Override
  public void receiveCommand(BookingMessage commandMessage) {
    System.out.println("Received Messasge: " + commandMessage.toString());
//    balanceMessagingService.processEventMessage(eventMessage);

  };

  @Override
  public void sendReply(EventReplyMessage replyMessage) {
    log.info("Publish Domain Event Booking");
    Message<EventReplyMessage> message = MessageBuilder
        .withPayload(replyMessage)
        .setHeader(KafkaHeaders.TOPIC, domainEventBookingTopic)
        .build();
    kafkaTemplate.send(message);

  };

}
