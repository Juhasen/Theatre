package pl.juhas.theater.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import pl.juhas.theater.enums.ReservationStatus;
import pl.juhas.theater.enums.TicketType;

import java.util.List;

@Data
@With
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @ManyToOne
    private User user;

    @OneToOne
    private Performance performance;

    @OneToMany
    private List<Seat> seat;

}
