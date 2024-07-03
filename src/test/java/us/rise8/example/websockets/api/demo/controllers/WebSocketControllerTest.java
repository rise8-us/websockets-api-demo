package us.rise8.example.websockets.api.demo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;

import us.rise8.example.websockets.api.demo.dtos.WebSocketMessageDTO;
import us.rise8.example.websockets.api.demo.services.WebSocketService;

@AutoConfigureMockMvc
@WebMvcTest(WebSocketController.class)
class WebSocketControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private WebSocketService webSocketService;

  @Test
  void postAutomationStart() throws Exception {
    mockMvc.perform(post("/automation")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("{\"topic\":\"topic/private\"}"))
      .andExpect(status().isOk())
      .andExpect(content().string("Automation Started"));

    verify(webSocketService).startAutomation(any(WebSocketMessageDTO.class));
  }

}
