package pl.juhas.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.juhas.theater.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
