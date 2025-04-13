package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.juhas.theater.model.Performance;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

}
