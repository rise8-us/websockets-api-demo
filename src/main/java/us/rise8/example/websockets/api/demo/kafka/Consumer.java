package us.rise8.example.websockets.api.demo.kafka;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import us.rise8.example.websockets.api.demo.dtos.KafkaMessageDTO;
import us.rise8.example.websockets.api.demo.utils.WebSocketHandler;

@Profile("kafka")
@Service
@RequiredArgsConstructor
public class Consumer implements MessageListener<String, KafkaMessageDTO> {

  private final WebSocketHandler webSocketHandler;

  @Override
  @KafkaListener(
      topics = "${kafka.topic}",
      groupId = "${kafka.group-id}",
      containerFactory = "listenerContainerFactory")
  public void onMessage(ConsumerRecord<String, KafkaMessageDTO> data) {
    if (data.value() == null) throw new AssertionError("Message cannot be null");

    KafkaMessageDTO kafkaMessage = data.value();

    webSocketHandler.sendMessage(kafkaMessage.getTopic(), kafkaMessage.getMessage());
  }
}
