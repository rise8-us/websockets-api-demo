package us.rise8.example.websockets.api.demo.services;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static us.rise8.example.websockets.api.demo.enums.Status.COMPLETED;
import static us.rise8.example.websockets.api.demo.enums.Status.ERRORED;
import static us.rise8.example.websockets.api.demo.enums.Status.IN_PROGRESS;
import static us.rise8.example.websockets.api.demo.enums.Status.STARTED;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;

import us.rise8.example.websockets.api.demo.dtos.WebSocketMessageDTO;
import us.rise8.example.websockets.api.demo.kafka.Producer;
import us.rise8.example.websockets.api.demo.utils.SleepWrapper;

@ActiveProfiles("kafka")
@Import({KafkaService.class})
@ExtendWith(SpringExtension.class)
class KafkaServiceTest {

  @SpyBean
  private KafkaService kafkaService;
  @MockBean
  private Producer producer;
  @MockBean
  private SleepWrapper sleepWrapper;

  private final WebSocketMessageDTO dto =
      WebSocketMessageDTO.builder().topic("topic").build();

  @Test
  void startKafkaAutomation_should_succeed() throws InterruptedException {
      doNothing().when(sleepWrapper).sleep(anyInt());

      kafkaService.startAutomation(dto);
      Thread.sleep(50); // wait for thread to finish

      InOrder inOrder = inOrder(producer);
      inOrder.verify(producer).sendMessage(argThat(message ->
          message.getMessage().getStatus().equals(STARTED) &&
              message.getMessage().getMessage().equals("Initializing") &&
              message.getTopic().equals(dto.getTopic())));
      inOrder.verify(producer).sendMessage(argThat(message ->
          message.getMessage().getStatus().equals(IN_PROGRESS) &&
              message.getMessage().getMessage().contains("Initializing complete") &&
              message.getTopic().equals(dto.getTopic())));
      inOrder.verify(producer).sendMessage(argThat(message ->
          message.getMessage().getStatus().equals(IN_PROGRESS) &&
              message.getMessage().getMessage().contains("Navigation check complete") &&
              message.getTopic().equals(dto.getTopic())));
      inOrder.verify(producer).sendMessage(argThat(message ->
          message.getMessage().getStatus().equals(IN_PROGRESS) &&
              message.getMessage().getMessage().contains("Life Support check complete") &&
              message.getTopic().equals(dto.getTopic())));
      inOrder.verify(producer).sendMessage(argThat(message ->
          message.getMessage().getStatus().equals(IN_PROGRESS) &&
              message.getMessage().getMessage().contains("Warp Core alignment complete") &&
              message.getTopic().equals(dto.getTopic())));
      inOrder.verify(producer).sendMessage(argThat(message ->
          message.getMessage().getStatus().equals(COMPLETED) &&
              message.getMessage().getMessage().contains("WebSocket Automation complete") &&
              message.getTopic().equals(dto.getTopic())));
  }

  @Test
  void startKafkaAutomation_should_fail() throws InterruptedException {
      doThrow(new InterruptedException()).when(sleepWrapper).sleep(anyInt());

      kafkaService.startAutomation(dto);
      Thread.sleep(50); // wait for thread to finish

      InOrder inOrder = inOrder(producer);
      inOrder.verify(producer).sendMessage(argThat(message ->
          message.getMessage().getStatus().equals(STARTED) &&
              message.getMessage().getMessage().equals("Initializing") &&
              message.getTopic().equals(dto.getTopic())));
      inOrder.verify(producer).sendMessage(argThat(message ->
          message.getMessage().getStatus().equals(ERRORED) &&
              message.getMessage().getMessage().contains("An error occurred. Please try again.") &&
              message.getTopic().equals(dto.getTopic())));
  }
}