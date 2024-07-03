package us.rise8.example.websockets.api.demo.services;

import static us.rise8.example.websockets.api.demo.enums.Status.COMPLETED;
import static us.rise8.example.websockets.api.demo.enums.Status.ERRORED;
import static us.rise8.example.websockets.api.demo.enums.Status.IN_PROGRESS;
import static us.rise8.example.websockets.api.demo.enums.Status.STARTED;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import us.rise8.example.websockets.api.demo.dtos.ProgressDTO;
import us.rise8.example.websockets.api.demo.dtos.WebSocketMessageDTO;
import us.rise8.example.websockets.api.demo.utils.SleepWrapper;
import us.rise8.example.websockets.api.demo.utils.WebSocketHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final WebSocketHandler webSocketHandler;
    private final SleepWrapper sleepWrapper;

    public void startAutomation(WebSocketMessageDTO webSocketMessageDTO) {
        String topic = webSocketMessageDTO.getTopic();

        Thread automationThread = new Thread(() -> {
            AtomicInteger currentTask = new AtomicInteger(0);

            try {
                webSocketHandler.sendMessage(
                        topic,
                        ProgressDTO.builder()
                                .status(STARTED)
                                .message("Initializing")
                                .currentTask(currentTask.getAndIncrement())
                                .totalTasks(5)
                                .build()
                );
                sleepWrapper.sleep(1000);
                webSocketHandler.sendMessage(
                        topic,
                        ProgressDTO.builder()
                                .status(IN_PROGRESS)
                                .message("Initializing complete. Beginning Comms check")
                                .currentTask(currentTask.getAndIncrement())
                                .totalTasks(5)
                                .build()
                );
                sleepWrapper.sleep(4000);
                webSocketHandler.sendMessage(
                        topic,
                        ProgressDTO.builder()
                                .status(IN_PROGRESS)
                                .message("Navigation check complete. Beginning Life Support check")
                                .currentTask(currentTask.getAndIncrement())
                                .totalTasks(5)
                                .build()
                );
                sleepWrapper.sleep(3000);
                webSocketHandler.sendMessage(
                        topic,
                        ProgressDTO.builder()
                                .status(IN_PROGRESS)
                                .message("Life Support check complete. Beginning Warp Core alignment")
                                .currentTask(currentTask.getAndIncrement())
                                .totalTasks(5)
                                .build()
                );
                sleepWrapper.sleep(5000);
                webSocketHandler.sendMessage(
                        topic,
                        ProgressDTO.builder()
                                .status(IN_PROGRESS)
                                .message("Warp Core alignment complete. Beginning Liftoff")
                                .currentTask(currentTask.getAndIncrement())
                                .totalTasks(5)
                                .build()
                );
                sleepWrapper.sleep(2000);
                webSocketHandler.sendMessage(
                        topic,
                        ProgressDTO.builder()
                                .status(COMPLETED)
                                .message("Liftoff complete. WebSocket Automation complete.")
                                .currentTask(currentTask.get())
                                .totalTasks(5)
                                .build()
                );
            } catch (InterruptedException e) {
                log.error("Error in Automation", e);

                webSocketHandler.sendMessage(
                    topic,
                    ProgressDTO.builder()
                        .status(ERRORED)
                        .message("An error occurred. Please try again.")
                        .currentTask(currentTask.get())
                        .totalTasks(5)
                        .build()
                );

                Thread.currentThread().interrupt();
            }
        });

        automationThread.start();
    }
}
