package us.rise8.example.websockets.api.demo.services;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;

import us.rise8.example.websockets.api.demo.dtos.WebSocketMessageDTO;
import us.rise8.example.websockets.api.demo.utils.SleepWrapper;
import us.rise8.example.websockets.api.demo.utils.WebSocketHandler;

@Import({WebSocketService.class})
@ExtendWith(SpringExtension.class)
class WebSocketServiceTest {

  @SpyBean
  private WebSocketService webSocketService;

  @MockBean
  private WebSocketHandler webSocketHandler;
  @MockBean
  private SleepWrapper sleepWrapper;

  private final WebSocketMessageDTO dto =
      WebSocketMessageDTO.builder().topic("topic").build();

  @Test
  void startAutomation_should_succeed() throws InterruptedException {
    doNothing().when(sleepWrapper).sleep(anyInt());

    webSocketService.startAutomation(dto);
    Thread.sleep(50); // wait for thread to finish

    InOrder inOrder = inOrder(webSocketHandler);
    inOrder.verify(webSocketHandler).sendMessage(eq(dto.getTopic()), argThat(progress ->
        progress.getStatus().equals(STARTED) && progress.getMessage().equals("Initializing")));
    inOrder.verify(webSocketHandler).sendMessage(eq(dto.getTopic()), argThat(progress ->
        progress.getStatus().equals(IN_PROGRESS) && progress.getMessage().contains("Initializing complete")));
    inOrder.verify(webSocketHandler).sendMessage(eq(dto.getTopic()), argThat(progress ->
        progress.getStatus().equals(IN_PROGRESS) && progress.getMessage().contains("Navigation check complete")));
    inOrder.verify(webSocketHandler).sendMessage(eq(dto.getTopic()), argThat(progress ->
        progress.getStatus().equals(IN_PROGRESS) && progress.getMessage().contains("Life Support check complete")));
    inOrder.verify(webSocketHandler).sendMessage(eq(dto.getTopic()), argThat(progress ->
        progress.getStatus().equals(IN_PROGRESS) && progress.getMessage().contains("Warp Core alignment complete")));
    inOrder.verify(webSocketHandler).sendMessage(eq(dto.getTopic()), argThat(progress ->
        progress.getStatus().equals(COMPLETED) && progress.getMessage().contains("WebSocket Automation complete")));
  }

  @Test
  void startAutomation_should_fail() throws InterruptedException {
      doThrow(new InterruptedException()).when(sleepWrapper).sleep(anyInt());

      webSocketService.startAutomation(dto);
      Thread.sleep(50); // wait for thread to finish

      InOrder inOrder = inOrder(webSocketHandler);
      inOrder.verify(webSocketHandler).sendMessage(eq(dto.getTopic()), argThat(progress ->
          progress.getStatus().equals(STARTED) && progress.getMessage().equals("Initializing")));
      inOrder.verify(webSocketHandler).sendMessage(eq(dto.getTopic()), argThat(progress ->
          progress.getStatus().equals(ERRORED) && progress.getMessage().contains("An error occurred. Please try again.")));
  }

}