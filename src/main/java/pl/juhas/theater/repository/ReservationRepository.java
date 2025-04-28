package pl.juhas.theater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.juhas.theater.dto.PerformanceSummaryDTO;
import pl.juhas.theater.model.Reservation;
import pl.juhas.theater.model.User;
import pl.juhas.theater.model.Performance;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 4) Lista uczestników na przedstawieniu o danym id
    @Query("SELECT r.user FROM Reservation r " +
            "WHERE r.performance.id = :performanceId " +
            "AND r.status = 'CONFIRMED'")
    Page<User> findUsersByPerformanceId(@Param("performanceId") Long performanceId, Pageable pageable);

    // 5) Lista przedstawień uczestnika o danym id
    @Query("SELECT new pl.juhas.theater.dto.PerformanceSummaryDTO(" +
            "r.performance.id, r.performance.play.title, r.performance.startTime, r.performance.room.name) " +
            "FROM Reservation r " +
            "WHERE r.user.id = :userId " +
            "AND r.status = 'CONFIRMED'")
    Page<PerformanceSummaryDTO> findPerformanceSummariesByUserId(@Param("userId") Long userId, Pageable pageable);

    // 6) Lista przedstawień uczestnika o danym loginie
    @Query("SELECT new pl.juhas.theater.dto.PerformanceSummaryDTO(" +
            "r.performance.id, r.performance.play.title, r.performance.startTime, r.performance.room.name) " +
            "FROM Reservation r " +
            "WHERE r.user.email = :email " +
            "AND r.status = 'CONFIRMED'")
    Page<PerformanceSummaryDTO> findPerformanceSummariesByUserLogin(@Param("login") String login, Pageable pageable);
}
