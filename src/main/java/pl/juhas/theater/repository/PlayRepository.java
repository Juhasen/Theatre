package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.juhas.theater.model.Play;

public interface PlayRepository extends JpaRepository<Play, Integer> {
}
