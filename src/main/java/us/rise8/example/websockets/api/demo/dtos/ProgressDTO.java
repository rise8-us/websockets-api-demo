package us.rise8.example.websockets.api.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import us.rise8.example.websockets.api.demo.enums.Status;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressDTO {
    private Status status;
    private String message;
    private int currentTask;
    private int totalTasks;
}
