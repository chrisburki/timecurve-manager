package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import timecurvemanager.bookkeeping.domain.booking.BookingMessageOutHdl;
import timecurvemanager.bookkeeping.domain.booking.api.BookingDomainEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

@Service
@Slf4j
@Profile("int")
public class BookingMessageOutHdlInt implements BookingMessageOutHdl {

  private final KafkaTemplate<String, BookingDomainEvent> kafkaTemplate;

  @Value(value = "${spring.kafka.topic.domain-event-booking}")
  private String domainEventBookingTopic;

  @Value(value = "${spring.kafka.topic.reply-booking}")
  private String replyBookingTopic;

  public BookingMessageOutHdlInt(
      KafkaTemplate<String, BookingDomainEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void sendDomainEvent(BookingDomainEvent event) {
    log.debug("Publish Int Booking Domain Event");
    Message<BookingDomainEvent> message = MessageBuilder
        .withPayload(event)
        .setHeader(KafkaHeaders.TOPIC, domainEventBookingTopic)
        .build();
    kafkaTemplate.send(message);
  }

  @Override
  public void sendExternalEvent(BookingExternalEvent event) {
    log.info("Publish Int Booking External Event");
  }

  @Override
  public void sendReply(BookingExternalEvent event) {
    log.debug("Publish Int Booking External Event");
    Message<BookingExternalEvent> message = MessageBuilder
        .withPayload(event)
        .setHeader(KafkaHeaders.TOPIC, replyBookingTopic)
        .build();
    kafkaTemplate.send(message);
  }

  ;

}
