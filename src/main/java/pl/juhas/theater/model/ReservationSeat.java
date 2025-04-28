package pl.juhas.theater.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;


// Connector: Reservation <-> Seat <-> Performance
@Data
@With
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation_seat")
public class ReservationSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Seat seat;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Reservation reservation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Performance performance;
}
