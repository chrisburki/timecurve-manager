package timecurvemanager.infrastructure.persistence.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import timecurvemanager.domain.event.EventMessaging;
import timecurvemanager.domain.event.publish.EventPublish;

@Service
@Slf4j
@Profile("int")
public class EventMessagingIntImpl implements EventMessaging {

  private final KafkaTemplate<String, EventPublish> kafkaTemplate;
//private final KafkaTemplate<String, String> kafkaTemplate;

  public EventMessagingIntImpl(
      KafkaTemplate<String, EventPublish> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void sendEvent(EventPublish event) {
    log.info("Publish Integration Event");
    kafkaTemplate.send("tcmgr.events", event);
  }

}
