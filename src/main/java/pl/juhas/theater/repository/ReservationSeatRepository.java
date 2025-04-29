package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.juhas.theater.model.ReservationSeat;

import java.time.LocalDateTime;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

    @Query("SELECT COUNT(seat.id) " +
            "FROM ReservationSeat seat " +
            "WHERE seat.reservation.performance.startTime = :startTime " +
            "AND seat.reservation.performance.room.id = :roomId")
    Long countOccupiedSeats(@Param("roomId") Long roomId,
                            @Param("startTime") LocalDateTime startTime);



    @Query("SELECT COUNT(seat.id) " +
            "FROM ReservationSeat seat " +
            "WHERE seat.reservation.user.id = :userId " +
            "AND seat.reservation.createdAt BETWEEN :startDate AND :endDate")
    Long countTicketsBoughtByUserInDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
