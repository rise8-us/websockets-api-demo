package us.rise8.example.websockets.api.demo.utils;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import us.rise8.example.websockets.api.demo.dtos.ProgressDTO;

@Service
@RequiredArgsConstructor
public class WebSocketHandler {

    private final SimpMessagingTemplate template;

    public void sendMessage(String topic, ProgressDTO progressDTO) {
        template.convertAndSend(topic, progressDTO);
    }
}
