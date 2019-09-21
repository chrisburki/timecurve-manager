package timecurvemanager.bookkeeping.infrastructure.messaging.booking;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import timecurvemanager.bookkeeping.domain.booking.api.BookingCommand;
import timecurvemanager.bookkeeping.domain.booking.api.BookingDomainEvent;
import timecurvemanager.bookkeeping.domain.booking.api.BookingExternalEvent;

@Configuration
@Profile("int")
@Slf4j
@EnableKafka
public class BookingIntConfig {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapServerAddress;

  // Producer for Booking Domain Event
  @Bean
  public ProducerFactory<String, BookingDomainEvent> bookingDomainEventMessageProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, BookingDomainEvent> bookingDomainEventMessageKafkaTemplate() {
    return new KafkaTemplate<>(bookingDomainEventMessageProducerFactory());
  }

  // Producer for Booking External Event
  @Bean
  public ProducerFactory<String, BookingExternalEvent> bookingExternalEventMessageProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, BookingExternalEvent> bookingExternalEventMessageKafkaTemplate() {
    return new KafkaTemplate<>(bookingExternalEventMessageProducerFactory());
  }

  // Producer for Booking Reply
  @Bean
  public ProducerFactory<String, BookingExternalEvent> bookingReplyMessageProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, BookingExternalEvent> bookingReplyMessageKafkaTemplate() {
    return new KafkaTemplate<>(bookingReplyMessageProducerFactory());
  }

  // Consumer for Booking Command
  @Bean
  public ConsumerFactory<String, BookingCommand> bookingCommandMessageConsumerFactory() {
    JsonDeserializer<BookingCommand> deserializer = new JsonDeserializer<>(BookingCommand.class);
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeMapperForKey(true);

    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerAddress);
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "foo");
    configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
    return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, BookingCommand> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, BookingCommand> factory
        = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(bookingCommandMessageConsumerFactory());
    return factory;
  }

}
