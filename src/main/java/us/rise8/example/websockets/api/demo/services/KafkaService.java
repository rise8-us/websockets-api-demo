package us.rise8.example.websockets.api.demo.services;

import static us.rise8.example.websockets.api.demo.enums.Status.COMPLETED;
import static us.rise8.example.websockets.api.demo.enums.Status.ERRORED;
import static us.rise8.example.websockets.api.demo.enums.Status.IN_PROGRESS;
import static us.rise8.example.websockets.api.demo.enums.Status.STARTED;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import us.rise8.example.websockets.api.demo.dtos.KafkaMessageDTO;
import us.rise8.example.websockets.api.demo.dtos.ProgressDTO;
import us.rise8.example.websockets.api.demo.dtos.WebSocketMessageDTO;
import us.rise8.example.websockets.api.demo.kafka.Producer;
import us.rise8.example.websockets.api.demo.utils.SleepWrapper;

@Profile("kafka")
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

  private final Producer producer;
  private final SleepWrapper sleepWrapper;

  public void startAutomation(WebSocketMessageDTO webSocketMessageDTO) {
    String topic = webSocketMessageDTO.getTopic();

    Thread automationThread = new Thread(() -> {
      AtomicInteger currentTask = new AtomicInteger(0);

      try {
        producer.sendMessage(
            KafkaMessageDTO.builder()
                .topic(topic)
                .message(
                    ProgressDTO.builder()
                        .status(STARTED)
                        .message("Initializing")
                        .currentTask(currentTask.getAndIncrement())
                        .totalTasks(5)
                        .build()
                )
                .build());

        sleepWrapper.sleep(1000);

        producer.sendMessage(
            KafkaMessageDTO.builder()
                .topic(topic)
                .message(
                    ProgressDTO.builder()
                        .status(IN_PROGRESS)
                        .message("Initializing complete. Beginning Comms check")
                        .currentTask(currentTask.getAndIncrement())
                        .totalTasks(5)
                        .build()
                )
                .build());

        sleepWrapper.sleep(4000);

        producer.sendMessage(
            KafkaMessageDTO.builder()
                .topic(topic)
                .message(
                    ProgressDTO.builder()
                        .status(IN_PROGRESS)
                        .message("Navigation check complete. Beginning Life Support check")
                        .currentTask(currentTask.getAndIncrement())
                        .totalTasks(5)
                        .build()
                )
                .build());

        sleepWrapper.sleep(3000);

        producer.sendMessage(
            KafkaMessageDTO.builder()
                .topic(topic)
                .message(
                    ProgressDTO.builder()
                        .status(IN_PROGRESS)
                        .message("Life Support check complete. Beginning Warp Core alignment")
                        .currentTask(currentTask.getAndIncrement())
                        .totalTasks(5)
                        .build()
                )
                .build());

        sleepWrapper.sleep(5000);

        producer.sendMessage(
            KafkaMessageDTO.builder()
                .topic(topic)
                .message(
                    ProgressDTO.builder()
                        .status(IN_PROGRESS)
                        .message("Warp Core alignment complete. Beginning Liftoff")
                        .currentTask(currentTask.getAndIncrement())
                        .totalTasks(5)
                        .build()
                )
                .build());

        sleepWrapper.sleep(2000);

        producer.sendMessage(
            KafkaMessageDTO.builder()
                .topic(topic)
                .message(
                    ProgressDTO.builder()
                        .status(COMPLETED)
                        .message("Liftoff complete. WebSocket Automation complete.")
                        .currentTask(currentTask.get())
                        .totalTasks(5)
                        .build()
                )
                .build());
      } catch (InterruptedException e) {
        log.error("Error in Automation", e);

        producer.sendMessage(
            KafkaMessageDTO.builder()
                .topic(topic)
                .message(
                    ProgressDTO.builder()
                        .status(ERRORED)
                        .message("An error occurred. Please try again.")
                        .currentTask(currentTask.get())
                        .totalTasks(5)
                        .build()
                )
                .build());

        Thread.currentThread().interrupt();
      }
    });

    automationThread.start();
  }
}
