package pl.juhas.theater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.juhas.theater.dto.PerformanceSummaryDTO;
import pl.juhas.theater.model.Reservation;
import pl.juhas.theater.model.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(
            "SELECT r.user FROM Reservation r " +
            "WHERE r.performance.id = :performanceId ")
    Page<User> findUsersByPerformanceId(@Param("performanceId") Long performanceId, Pageable pageable);


    @Query("SELECT new pl.juhas.theater.dto.PerformanceSummaryDTO(" +
            "r.performance.id, r.performance.play.title, r.performance.startTime, r.performance.room.name) " +
            "FROM Reservation r " +
            "WHERE r.user.email = :email ")
    Page<PerformanceSummaryDTO> findPerformanceByUserEmail(@Param("email") String login, Pageable pageable);

    @Query("SELECT new pl.juhas.theater.dto.PerformanceSummaryDTO(" +
            "r.performance.id, r.performance.play.title, r.performance.startTime, r.performance.room.name) " +
            "FROM Reservation r " +
            "WHERE r.user.id = :id ")
    Page<PerformanceSummaryDTO> findPerformancesByUserId(Long id, Pageable pageable);


}
