package timecurvemanager.infrastructure.persistence.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.EventMessaging;
import timecurvemanager.domain.event.publish.EventPublish;

@Component
@Slf4j
@Profile("prod")
public class EventMessagingProdImpl implements EventMessaging {

  @Override
  public void sendEvent(EventPublish event) {
    log.info("Publish Production Event");
  }
}