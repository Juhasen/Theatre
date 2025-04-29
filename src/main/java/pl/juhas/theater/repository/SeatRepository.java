package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.juhas.theater.model.Seat;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
}
