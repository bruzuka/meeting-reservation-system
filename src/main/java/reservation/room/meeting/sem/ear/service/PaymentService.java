package reservation.room.meeting.sem.ear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reservation.room.meeting.sem.ear.entity.Payment;

import reservation.room.meeting.sem.ear.entity.Reservation;
import reservation.room.meeting.sem.ear.exception.InvalidFieldException;
import reservation.room.meeting.sem.ear.exception.MissingFieldException;
import reservation.room.meeting.sem.ear.repository.PaymentRepository;
import reservation.room.meeting.sem.ear.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public PaymentService(PaymentRepository repository, ReservationRepository reservationRepository) {
        this.repository = repository;
        this.reservationRepository = reservationRepository;
    }

    public Iterable<Payment> getAllPayments() {
        Iterable<Payment> payments = repository.findAll();
        for (Payment payment : payments) {
            addTotalPrice(payment);
        }
        return payments;
    }

    public Optional<Payment> getPayment(Long paymentId) {
        Payment payment = repository.findById(paymentId).orElseThrow(
                () -> new InvalidFieldException("Payment whit id " + paymentId + " odes not exist"));
        addTotalPrice(payment);
        return Optional.of(payment);
    }

    public Payment createPayment(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new InvalidFieldException("Reservation with id " + reservationId + " does not exist."));
        Payment payment = new Payment();
        reservation.setPayment(payment);
        addTotalPrice(payment);
        payment.setDateCreates(LocalDateTime.now());
        return repository.save(payment);
    }


    public void toPaymentAddReservation(Long id, Long reservationId) {
        Payment payment = repository.findById(id).orElseThrow(
                () -> new InvalidFieldException("Payment whit id " + id + " odes not exist"));
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new InvalidFieldException("Reservation with id " + reservationId + " does not exist."));
        reservation.setPayment(payment);
        repository.save(payment);
    }


    public void addTotalPrice(Payment payment) {
        Set<Reservation> reservations = payment.getReservations();
        Double totalPrice = 0D;
        if (reservations != null) {
            for (Reservation reservation : reservations) {
                totalPrice += reservation.getPrice();
            }
        } else {
            throw new MissingFieldException();
        }
        payment.setTotalPrice(totalPrice);
        repository.save(payment);
    }


    public void deletePayment(Long paymentId) {
        boolean exists = repository.existsById(paymentId);

        if (!exists) {
            throw new InvalidFieldException("Payment with id " + paymentId + " does not exist.");
        }
        repository.deleteById(paymentId);
    }

    public Iterable<Payment> showAllByDate(LocalDateTime dateCreates, LocalDateTime dateCreates2) {
        return repository.findAllByDateCreatesBetween(dateCreates, dateCreates2);
    }

}
