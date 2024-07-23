package us.rise8.example.websockets.api.demo.acceptance;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import us.rise8.example.websockets.api.demo.dtos.ProgressDTO;
import us.rise8.example.websockets.api.demo.utils.SleepWrapper;
import us.rise8.example.websockets.api.demo.utils.WebSocketHandler;

@ActiveProfiles("kafka")
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class KafkaTests {

  static final String TOPIC = "test.topic";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SleepWrapper sleepWrapper;
  @MockBean
  private WebSocketHandler webSocketHandler;

  @Container
  static final KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    registry.add("spring.kafka.properties.security.protocol", () -> "PLAINTEXT");
  }

  @BeforeAll
  static void initializeXmlTransactions() {
    kafka.start();
    await().atMost(30, SECONDS).until(kafka::isRunning);
  }

  @AfterAll
  static void tearDown() {
    kafka.stop();
  }


  @Test
  void should_process_kafka_messages() throws Exception {
    doNothing().when(sleepWrapper).sleep(anyInt());

    this.mockMvc.perform(
        post("/kafka/automation")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("{\"topic\":\"" + TOPIC + "\"}"));

    await()
        .pollInterval(2, SECONDS)
        .atMost(30, SECONDS)
        .untilAsserted(() -> verify(webSocketHandler, times(6)).sendMessage(eq(TOPIC), any(ProgressDTO.class)));
  }
}
