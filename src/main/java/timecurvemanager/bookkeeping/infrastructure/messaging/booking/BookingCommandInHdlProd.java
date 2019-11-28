package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.pubsub.v1.PubsubMessage;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import timecurvemanager.bookkeeping.application.BookingService;
import timecurvemanager.bookkeeping.domain.booking.BookingCommandInHdl;
import timecurvemanager.bookkeeping.domain.booking.BookingMessageOutHdl;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

@Component
@Slf4j
@Profile("prod")
public class BookingCommandInHdlProd implements BookingCommandInHdl {

  private final BookingService bookingService;
  private final BookingMessageOutHdl bookingMessageOutHdl;

  public BookingCommandInHdlProd(
      BookingService bookingService,
      BookingMessageOutHdl bookingMessageOutHdl) {
    this.bookingService = bookingService;
    this.bookingMessageOutHdl = bookingMessageOutHdl;
  }

  @Override
  public void receiveCommand(BookingCommand event, MessageHeaders headers) {
    log.info("Receive Prod Booking Command");
  }

  protected void consumeCommand(BasicAcknowledgeablePubsubMessage command) {
    // extract wrapped message
    PubsubMessage message = command.getPubsubMessage();
    Map<String, String> attributeMap = message.getAttributesMap();

    String replyTopicVal = "";
    String correlationIdVal = "";

    if (attributeMap.containsKey(BookingProdConfig.replyTopic)) {
      replyTopicVal = attributeMap.get(BookingProdConfig.replyTopic);
    }
    if (attributeMap.containsKey(BookingProdConfig.correlationId)) {
      correlationIdVal = attributeMap.get(BookingProdConfig.correlationId);
    }

    // process message
    String messageJson = message.getData().toStringUtf8();
    log.info("message received: replTopic: " + replyTopicVal + " corrId: "+ correlationIdVal + " msg: " + messageJson);

    // acknowledge that message was received
    command.ack();

    // converted into domain event
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      BookingCommand bookingCommand = objectMapper
          .readValue(messageJson, BookingCommand.class);
      BookingExternalEvent bookingExternalEvent = bookingService
          .processBookingCommand(bookingCommand);
      if (replyTopicVal != null) {
        bookingMessageOutHdl.sendReply(bookingExternalEvent, replyTopicVal, correlationIdVal);
      }
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Implementation of a {@link Consumer} functional interface.
   */
  public Consumer<BasicAcknowledgeablePubsubMessage> consumer() {
    return basicAcknowledgeablePubsubMessage -> consumeCommand(basicAcknowledgeablePubsubMessage);
  }
}
