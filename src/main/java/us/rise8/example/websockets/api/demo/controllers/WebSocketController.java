package us.rise8.example.websockets.api.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import us.rise8.example.websockets.api.demo.dtos.WebSocketMessageDTO;
import us.rise8.example.websockets.api.demo.services.WebSocketService;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketService webSocketService;

    @PostMapping("/automation")
    public ResponseEntity<String> postAutomationStart(@RequestBody WebSocketMessageDTO webSocketMessageDTO) {
        webSocketService.startAutomation(webSocketMessageDTO);
        return ResponseEntity.ok("Automation Started");
    }
}
