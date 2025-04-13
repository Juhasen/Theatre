package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.juhas.theater.model.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {

}
