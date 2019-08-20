package timecurvemanager.infrastructure.messaging.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.messaging.EventMessage;

@Service
@Slf4j
@Profile("int")
public class EventMessagingIntImpl implements timecurvemanager.domain.event.EventMessaging {

  private final KafkaTemplate<String, EventMessage> kafkaTemplate;

  @Value(value = "${spring.kafka.template.default-topic}")
  private String topic;

  public EventMessagingIntImpl(
      KafkaTemplate<String, EventMessage> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void sendEvent(EventMessage event) {
    log.info("Publish Integration Event");
    Message<EventMessage> message = MessageBuilder
        .withPayload(event)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .build();
    kafkaTemplate.send(message);
  }

}
