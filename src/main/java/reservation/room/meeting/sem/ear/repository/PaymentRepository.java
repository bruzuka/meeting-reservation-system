package reservation.room.meeting.sem.ear.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reservation.room.meeting.sem.ear.entity.Payment;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {

    Set<Payment> findAllByDateCreatesBetween(LocalDateTime dateCreates, LocalDateTime dateCreates2);
}
