package us.rise8.example.websockets.api.demo.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;

import us.rise8.example.websockets.api.demo.dtos.KafkaMessageDTO;

@Profile("kafka")
@Service
@RequiredArgsConstructor
public class Producer {

  @Value("${kafka.topic}")
  private String topic;

  private final KafkaTemplate<String, KafkaMessageDTO> kafkaTemplate;

  public void sendMessage(KafkaMessageDTO message) {
    kafkaTemplate.send(new ProducerRecord<>(topic, message));
  }
}
