package reservation.room.meeting.sem.ear.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reservation.room.meeting.sem.ear.entity.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {
}
