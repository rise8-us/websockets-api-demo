package us.rise8.example.websockets.api.demo.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import us.rise8.example.websockets.api.demo.dtos.KafkaMessageDTO;

@Profile("kafka")
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  private final ObjectMapper objectMapper;

  @Bean
  public KafkaTemplate<String, KafkaMessageDTO> kafkaTemplate(
      ProducerFactory<String, KafkaMessageDTO> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, KafkaMessageDTO>
  listenerContainerFactory(ConsumerFactory<String, KafkaMessageDTO> consumerFactory) {
    return getKafkaListenerContainerFactory(consumerFactory);
  }

  @Bean
  public ConsumerFactory<String, KafkaMessageDTO> consumerFactory(
      KafkaProperties consumerProps,
      @Value("${kafka.group-id}") String groupId) {
    return new DefaultKafkaConsumerFactory<>(
        getProps(consumerProps, groupId),
        StringDeserializer::new,
        () -> new JsonDeserializer<>(KafkaMessageDTO.class));
  }

  @Bean(name = "producerFactory")
  public ProducerFactory<String, KafkaMessageDTO> producerFactory(
      KafkaProperties producerProps, @Value("${kafka.group-id}") String groupId) {
    return new DefaultKafkaProducerFactory<>(
        getProps(producerProps, groupId),
        StringSerializer::new,
        () -> new JsonSerializer<>(objectMapper));
  }

  private Map<String, Object> getProps(KafkaProperties consumerProps, String groupId) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProps.getBootstrapServers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.putAll(consumerProps.getProperties());
    return props;
  }

  private <T> ConcurrentKafkaListenerContainerFactory<String, T> getKafkaListenerContainerFactory(
      ConsumerFactory<String, T> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, T> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }

  @Bean
  public NewTopic createTopic(@Value("${kafka.topic}") String topic) {
    return TopicBuilder.name(topic).partitions(2).replicas(1).build();
  }

}
