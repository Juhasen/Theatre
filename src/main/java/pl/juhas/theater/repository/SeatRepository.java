package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.juhas.theater.model.ReservationSeat;

import java.time.LocalDateTime;

public interface SeatRepository extends JpaRepository<ReservationSeat, Long> {

    // 7) Liczba miejsc zajętych w danej sali o danej godzinie
    @Query("SELECT COUNT(seat.id) " +
            "FROM ReservationSeat seat " +
            "WHERE seat.reservation.performance.startTime = :time " +
            "AND seat.reservation.performance.room.id = :roomId " +
            "AND seat.reservation.status = 'CONFIRMED'")
    Long countOccupiedSeats(@Param("roomId") Long roomId, @Param("time") LocalDateTime time);

    // 9) Liczba miejsc kupionych w teatrze przez użytkownika w danym przedziale dat
    @Query("SELECT COUNT(seat.id) " +
            "FROM ReservationSeat seat " +
            "WHERE seat.reservation.user.id = :userId " +
            "AND seat.reservation.createdAt BETWEEN :startDate AND :endDate " +
            "AND seat.reservation.status = 'CONFIRMED'")
    Long countTicketsBoughtByUserInDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
