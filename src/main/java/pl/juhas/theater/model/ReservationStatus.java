package pl.juhas.theater.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;


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

    private String status;
}
