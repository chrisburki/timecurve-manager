package timecurvemanager.infrastructure.persistence.event;


import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import timecurvemanager.domain.event.publish.EventPublish;

@Configuration
@Slf4j
@Profile("int")
public class EventMessagingIntConfig {

  @Bean
  public ProducerFactory<String, EventPublish> eventPublishProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, EventPublish> eventPublishKafkaTemplate() {
    return new KafkaTemplate<>(eventPublishProducerFactory());
  }
}
