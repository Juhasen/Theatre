package pl.juhas.theater.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@With
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation_status")
public class ReservationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public ReservationStatus(String status) {
        this.name = status;
    }
}
