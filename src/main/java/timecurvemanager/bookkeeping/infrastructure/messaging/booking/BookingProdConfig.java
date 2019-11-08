package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.rpc.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.PubSubAdmin;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@Slf4j
public class BookingProdConfig {

  @Value(value = "${topic.domain-event-booking}")
  private String topicDomainEvent;

  @Value(value = "${topic.command-booking}")
  private String topicCommand;

  @Value(value = "${topic.reply-booking}")
  private String topicReply;

  final String subscriptionDomainEvent = "bookingDomainEvent";
  final String subscriptionCommand = "bookingCommandSub";
  final String subscriptionReply = "bookingReply";

  private final PubSubAdmin pubSubAdmin;

  public BookingProdConfig(PubSubAdmin pubSubAdmin) {
    this.pubSubAdmin = pubSubAdmin;
  }

  @Bean
  public JacksonPubSubMessageConverter jacksonPubSubMessageConverter(ObjectMapper objectMapper) {
    return new JacksonPubSubMessageConverter(objectMapper);
  }

  @Bean
  public void createTopics() {
    try {
      pubSubAdmin.createTopic(topicDomainEvent);
      pubSubAdmin.createTopic(topicCommand);
      pubSubAdmin.createTopic(topicReply);
    } catch (ApiException e) {
      System.out.print(e.getStatusCode().getCode());
      System.out.print(e.isRetryable());
//      if (e.getStatusCode() != Status.Code.ALREADY_EXISTS) {
//        throw e;
//      }
    }
  }

  @Bean
  public void createSubscription() {
    try {
//    pubSubAdmin.createSubscription(subscriptionDomainEvent, topicDomainEvent);
      pubSubAdmin.createSubscription(subscriptionCommand, topicCommand);
//    pubSubAdmin.createSubscription(subscriptionReply, topicReply);
    } catch (ApiException e) {
      System.out.print(e.getStatusCode().getCode());
      System.out.print(e.isRetryable());
//      if (e.getStatusCode() != Status.Code.ALREADY_EXISTS) {
//        throw e;
//      }
    }
  }

}
