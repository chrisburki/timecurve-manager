package timecurvemanager.infrastructure.messaging.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import timecurvemanager.domain.event.messaging.BookingMessage;
import timecurvemanager.domain.event.messaging.BookingDomainEventMessage;
import timecurvemanager.domain.event.messaging.EventReplyMessage;

@Component
@Slf4j
@Profile("test")
public class EventMessagingTestImpl implements timecurvemanager.domain.event.EventMessaging {

  @Override
  public void sendDomainEvent(BookingDomainEventMessage message) {
    log.info("Publish Test Event");
  }

  @Override
  public void receiveCommand(BookingMessage commandMessage) {

  }

  @Override
  public void sendReply(EventReplyMessage replyMessage) {

  }

}
