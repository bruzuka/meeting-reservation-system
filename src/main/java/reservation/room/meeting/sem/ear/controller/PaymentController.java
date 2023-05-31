package reservation.room.meeting.sem.ear.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reservation.room.meeting.sem.ear.dto.request.PaymentRequest;
import reservation.room.meeting.sem.ear.entity.Payment;
import reservation.room.meeting.sem.ear.exception.NotFoundException;
import reservation.room.meeting.sem.ear.service.PaymentService;

import java.util.Optional;

@RestController
public class PaymentController {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService service;

    @Autowired
    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/all/payments")
    public ResponseEntity<Iterable<Payment>> getAllPayments() {
        return ResponseEntity.ok(service.getAllPayments());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/payments/{id}")
    public ResponseEntity<Optional<Payment>> getPayment(@PathVariable("id") Long reservationId) {
        if (reservationId == null) {
            throw new NotFoundException("PAYMENT_NOT_FOUND");
        }
        return ResponseEntity.ok(service.getPayment(reservationId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/all/by/date")
    public ResponseEntity<Iterable<Payment>> getAllPaymentsByDate(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(
                service.showAllByDate(
                        paymentRequest.reservationDateTimeStart(),
                        paymentRequest.reservationDateTimeStart()
                )
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/create/payment")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest paymentRequest) {
        if (paymentRequest.reservationId() == null) {
            throw new NotFoundException("PAYMENT_NOT_FOUND");
        }
        LOG.debug("Payment {} successfully created", paymentRequest.id());
        return ResponseEntity.ok(service.createPayment(paymentRequest.reservationId()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/payment/add/reservation")
    public void toPaymentAddReservation(@RequestBody PaymentRequest paymentRequest) {
        if (paymentRequest.id() == null) {
            throw new NotFoundException("PAYMENT_NOT_FOUND");
        }
        if (paymentRequest.reservationId() == null) {
            throw new NotFoundException("RESERVATION_NOT_FOUND");
        }
        LOG.debug(
                "Payment {} successfully added to reservation {}", paymentRequest.id(), paymentRequest.reservationId()
        );
        service.toPaymentAddReservation(paymentRequest.id(), paymentRequest.reservationId());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/payment/{id}")
    public void deletePayment(@PathVariable("id") Long paymentId) {
        if (paymentId == null) {
            throw new NotFoundException("PAYMENT_NOT_FOUND");
        }
        LOG.debug("Payment {} successfully deleted", paymentId);
        service.deletePayment(paymentId);
    }
}