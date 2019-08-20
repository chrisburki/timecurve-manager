package timecurvemanager.infrastructure.messaging.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.messaging.EventMessage;

@Component
@Slf4j
@Profile("prod")
public class EventMessagingProdImpl implements timecurvemanager.domain.event.EventMessaging {

  @Override
  public void sendEvent(EventMessage event) {
    log.info("Publish Production Event");
  }
}