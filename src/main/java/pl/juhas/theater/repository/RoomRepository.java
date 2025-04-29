package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.juhas.theater.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}