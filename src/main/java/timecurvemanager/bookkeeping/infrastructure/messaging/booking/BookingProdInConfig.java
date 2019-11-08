package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

@Configuration
@Profile("prod")
@Slf4j
public class BookingProdInConfig {

  @Value(value = "${topic.command-booking}")
  private String topicCommand;

  private final BookingCommandInHdlProd bookingCommandInHdlProd;
  private final PubSubTemplate pubSubTemplate;
  private final BookingProdConfig bookingProdConfig;

  public BookingProdInConfig(
      BookingCommandInHdlProd bookingCommandInHdlProd,
      PubSubTemplate pubSubTemplate,
      BookingProdConfig bookingProdConfig) {
    this.bookingCommandInHdlProd = bookingCommandInHdlProd;
    this.pubSubTemplate = pubSubTemplate;
    this.bookingProdConfig = bookingProdConfig;
  }

  /**
   * It's called only when the application is ready to receive requests. Passes a consumer
   * implementation when subscribing to a Pub/Sub topic.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void subscribe() {
    log.info("Subscribing {} to {}", bookingCommandInHdlProd.getClass().getSimpleName(),
        bookingProdConfig.subscriptionCommand);
    pubSubTemplate.subscribe(bookingProdConfig.subscriptionCommand, bookingCommandInHdlProd.consumer());
  }
}
