package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.juhas.theater.model.TicketType;

public interface TicketTypeRepository extends JpaRepository<TicketType, Integer> {

    TicketType findByType(String type);
}
