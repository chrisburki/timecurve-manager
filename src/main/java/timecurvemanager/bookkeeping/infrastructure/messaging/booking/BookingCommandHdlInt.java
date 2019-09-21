package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import timecurvemanager.bookkeeping.application.BookingService;
import timecurvemanager.bookkeeping.domain.booking.BookingCommandHdl;
import timecurvemanager.bookkeeping.domain.booking.BookingMessaging;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingMessage;

@Service
@Slf4j
@Profile("int")
public class BookingCommandHdlInt implements BookingCommandHdl {

  private final BookingService bookingService;
  private final BookingMessaging bookingMessaging;

  public BookingCommandHdlInt(
      BookingService bookingService,
      BookingMessaging bookingMessaging) {
    this.bookingService = bookingService;
    this.bookingMessaging = bookingMessaging;
  }

  @KafkaListener(topics = "${spring.kafka.topic.command-booking}")
  //, groupId = "foo", containerFactory = "bookingContainerFactory")
  @Override
  public void receiveCommand(@Payload BookingCommand event, @Headers MessageHeaders headers) {
    System.out.println("Received Int Booking Command: " + event.toString());
    Object replyChannel = headers.getReplyChannel();
    BookingExternalEvent bookingExternalEvent = bookingService
        .processBookingCommand(event);
    if (replyChannel != null) {
      bookingMessaging.sendReply(bookingExternalEvent);
    }
  }
}
