package pl.juhas.theater.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@With
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "performance")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "play_id", nullable = false)
    private Play play;

}
