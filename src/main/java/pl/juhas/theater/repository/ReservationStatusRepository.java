package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.juhas.theater.model.ReservationStatus;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
    ReservationStatus findByName(String type);
}
