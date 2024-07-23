package us.rise8.example.websockets.api.demo.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import us.rise8.example.websockets.api.demo.dtos.WebSocketMessageDTO;
import us.rise8.example.websockets.api.demo.services.KafkaService;

@Profile("kafka")
@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaService kafkaService;

    @PostMapping("/kafka/automation")
    public ResponseEntity<String> postKafkaAutomationStart(@RequestBody WebSocketMessageDTO webSocketMessageDTO) {
        kafkaService.startAutomation(webSocketMessageDTO);
        return ResponseEntity.ok("Kafka Automation Started");
    }
}
