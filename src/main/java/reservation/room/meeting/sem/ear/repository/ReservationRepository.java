package reservation.room.meeting.sem.ear.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reservation.room.meeting.sem.ear.entity.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
}
