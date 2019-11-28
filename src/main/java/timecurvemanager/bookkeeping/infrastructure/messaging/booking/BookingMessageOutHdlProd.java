package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.BookingMessageOutHdl;
import timecurvemanager.bookkeeping.domain.booking.api.BookingDomainEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

@Component
@Slf4j
@Profile("prod")
public class BookingMessageOutHdlProd implements BookingMessageOutHdl {

  @Value(value = "${topic.domain-event-booking}")
  private String topicDomainEvent;

  @Value(value = "${topic.reply-booking}")
  private String topicReply;

  private final PubSubTemplate pubSubTemplate;

  public BookingMessageOutHdlProd(
      PubSubTemplate pubSubTemplate) {
    this.pubSubTemplate = pubSubTemplate;
  }

  @Override
  public void sendDomainEvent(BookingDomainEvent event) {
    log.info("publish a booking domain event message==[{}]", event);
/*
    Message<BookingDomainEvent> message = MessageBuilder
        .withPayload(event)
        .setHeader(KafkaHeaders.TOPIC, topicDomainEvent)
        .build();
    pubSubTemplate.publish(topicDomainEvent,message);

    PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
        .setData(ByteString.copyFromUtf8(event.toString()))
        .putAttributes(KafkaHeaders.TOPIC, topicDomainEvent)
        .build();
 */
    pubSubTemplate.publish(topicDomainEvent, event);
  }

  @Override
  public void sendExternalEvent(BookingExternalEvent event) {
    log.info("Publish Prod Booking External Event");
  }

  @Override
  public void sendReply(BookingExternalEvent event, String replyTopic, String correlationId) {
    log.info(
        "Publish Booking Reply Event ==[{}]: rplyT:" + replyTopic + " corrId: " + correlationId,
        event);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      PubsubMessage pubsubMessage =
          PubsubMessage.newBuilder()
              .setData(ByteString.copyFromUtf8(objectMapper.writeValueAsString(event)))
              .putAttributes(BookingProdConfig.replyTopic,
                  replyTopic)
              .putAttributes(BookingProdConfig.correlationId, correlationId)
              .build();
      log.info("write msg: " + objectMapper.writeValueAsString(event));
      pubSubTemplate.publish(replyTopic, pubsubMessage);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    /*
    Message<BookingExternalEvent> message = MessageBuilder
        .withPayload(event)
        .setHeader(BookingProdConfig.replyTopic, Optional.ofNullable(replyTopic).orElse(topicReply))
        .setHeader(BookingProdConfig.correlationId, correlationId)
        .build();
    pubSubTemplate.publish(topicReply, message);
*/
  }
}