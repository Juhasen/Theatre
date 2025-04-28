package pl.juhas.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceSummaryDTO {
    private Long id;
    private String title;
    private LocalDateTime startTime;
    private String roomName;
}
