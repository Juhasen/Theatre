package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.juhas.theater.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // 8) Liczba sal, w których wystawiano przedstawienie o danym id
    @Query("SELECT COUNT(DISTINCT p.room.id) FROM Performance p WHERE p.play.id = :playId")
    Long countDistinctRoomsByPlayId(@Param("playId") Long playId);
}