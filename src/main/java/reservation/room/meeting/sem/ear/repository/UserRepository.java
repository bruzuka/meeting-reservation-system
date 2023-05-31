package reservation.room.meeting.sem.ear.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reservation.room.meeting.sem.ear.entity.User;

import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Set<User> findAllByIdIsNotNullOrderByUsernameAsc();

    Set<User> findAllByIdIsNotNullOrderByUsernameDesc();

    User findByEmail(String email);

    User findByUsername(String username);
}
