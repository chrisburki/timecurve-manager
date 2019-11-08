package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import com.google.pubsub.v1.PubsubMessage;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.domain.booking.BookingCommandInHdl;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;

@Component
@Slf4j
@Profile("prod")
public class BookingCommandInHdlProd implements BookingCommandInHdl {

  @Override
  public void receiveCommand(BookingCommand event, MessageHeaders headers) {
    log.info("Receive Prod Booking Command");
  }

  protected void consumeCommand(BasicAcknowledgeablePubsubMessage command) {
    // extract wrapped message
    PubsubMessage message = command.getPubsubMessage();

    // process message
    log.info("message received: " + message.getData().toStringUtf8());

    // acknowledge that message was received
    command.ack();
  }

  /**
   * Implementation of a {@link Consumer} functional interface.
   */
  public Consumer<BasicAcknowledgeablePubsubMessage> consumer() {
    return basicAcknowledgeablePubsubMessage -> consumeCommand(basicAcknowledgeablePubsubMessage);
  }
}
