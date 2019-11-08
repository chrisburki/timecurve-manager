package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import timecurvemanager.bookkeeping.application.BookingService;
import timecurvemanager.bookkeeping.domain.booking.BookingCommandInHdl;
import timecurvemanager.bookkeeping.domain.booking.BookingMessageOutHdl;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

@Service
@Slf4j
@Profile("int")
public class BookingCommandInHdlInt implements BookingCommandInHdl {

  private final BookingService bookingService;
  private final BookingMessageOutHdl bookingMessageOutHdl;

  public BookingCommandInHdlInt(
      BookingService bookingService,
      BookingMessageOutHdl bookingMessageOutHdl) {
    this.bookingService = bookingService;
    this.bookingMessageOutHdl = bookingMessageOutHdl;
  }

  @KafkaListener(topics = "${spring.kafka.topic.command-booking}")
  //, groupId = "foo", containerFactory = "bookingContainerFactory")
  @Override
  public void receiveCommand(@Payload BookingCommand event, @Headers MessageHeaders headers) {
    System.out.println("Received Int Booking Command: " + event.toString());
    String replyTopic = headers.get(KafkaHeaders.REPLY_TOPIC).toString();
    String correlationId = headers.get(KafkaHeaders.CORRELATION_ID).toString();
    BookingExternalEvent bookingExternalEvent = bookingService
        .processBookingCommand(event);
    if (replyTopic != null) {
      bookingMessageOutHdl.sendReply(bookingExternalEvent, replyTopic, correlationId);
    }
  }
}
