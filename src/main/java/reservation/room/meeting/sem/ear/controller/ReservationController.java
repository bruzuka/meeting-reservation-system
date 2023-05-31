package reservation.room.meeting.sem.ear.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reservation.room.meeting.sem.ear.dto.request.ReservationRequest;
import reservation.room.meeting.sem.ear.entity.Reservation;
import reservation.room.meeting.sem.ear.exception.NotFoundException;
import reservation.room.meeting.sem.ear.service.ReservationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ReservationController {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService service;

    @Autowired
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /*
    * ROLE_ADMIN usage Reservation
    */

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/get/all/reservations")
    public ResponseEntity<Iterable<Reservation>> getAllReservations() {
        return ResponseEntity.ok(service.getAllReservations());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/get/reservation/{id}")
    public ResponseEntity<Optional<Reservation>> getReservation(@PathVariable("id") Long reservationId) {
        if (reservationId == null) {
            throw new NotFoundException("RESERVATION_NOT_FOUND");
        }
        return ResponseEntity.ok(service.getReservation(reservationId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/get/all/reservations/between")
    public ResponseEntity<Iterable<Reservation>> getAllReservationsBetween(
            @RequestBody ReservationRequest reservationRequest) {
        return ResponseEntity.ok(service.getAllReservationsBetween(
                        reservationRequest.reservationDateTimeStart(), reservationRequest.reservationDateTimeEnd()
                )
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/reservations/top/{num}/asc")
    public ResponseEntity<ArrayList<Reservation>> getTopReservationsByNumAsc(@PathVariable Integer num) {
        if (num == null) {
            throw new NotFoundException("RESERVATIONS_TOP_ASC_NOT_FOUND");
        }
        return ResponseEntity.ok(service.findTopReservationsByNumAsc(num));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/reservations/top/{num}/desc")
    public ResponseEntity<ArrayList<Reservation>> getTopReservationsByNumDesc(@PathVariable Integer num) {
        if (num == null) {
            throw new NotFoundException("RESERVATIONS_TOP_DESC_NOT_FOUND");
        }
        return ResponseEntity.ok(service.findTopReservationsByNumDesc(num));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/all/reservations/time/after/{time}")
    public ResponseEntity<Iterable<Reservation>> getReservationsByDateTimeAfter(
            @PathVariable LocalDateTime time) {
        if (time == null) {
            throw new NotFoundException("START_TIME_NOT_FOUND");
        }
        return ResponseEntity.ok(service.getAllReservationsByDateTimeAfter(time));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/all/reservations/time/before/{time}")
    public ResponseEntity<Iterable<Reservation>> getReservationsByDateTimeBefore(@PathVariable LocalDateTime time) {
        if (time == null) {
            throw new NotFoundException("END_TIME_NOT_FOUND");
        }
        return ResponseEntity.ok(service.getAllReservationsByDateTimeBefore(time));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/create/reservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest reservationRequest) {
        if (reservationRequest.reservationDateTimeStart() == null
                || reservationRequest.reservationDateTimeEnd() == null) {
            throw new NotFoundException("RESERVATION_PARAMETERS_NOT_FOUND");
        }
        LOG.debug("Reservation {} successfully created", reservationRequest.roomId());
        return ResponseEntity.ok(
                service.createReservation(
                        reservationRequest.reservationDateTimeStart(),
                        reservationRequest.reservationDateTimeEnd())
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/reservation/add/room")
    public void toReservationAddRoom(
            @RequestBody ReservationRequest reservationRequest) {
        if (reservationRequest.id() == null) {
            throw new NotFoundException("RESERVATION_NOT_FOUND");
        }
        if (reservationRequest.roomId() == null) {
            throw new NotFoundException("ROOM_NOT_FOUND");
        }
        LOG.debug(
                "Reservation {} successfully added to room {}", reservationRequest.id(), reservationRequest.roomId()
        );
        service.toReservationAddRoom(reservationRequest.id(), reservationRequest.roomId());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/change/reservation/{id}")
    public void updateReservation(
            @PathVariable("id") Long reservationId,
            @RequestBody ReservationRequest reservationRequest) {
        if (reservationId == null) {
            throw new NotFoundException("RESERVATION_NOT_FOUND");
        }
        if (reservationRequest.reservationDateTimeStart() == null
                || reservationRequest.reservationDateTimeEnd() == null) {
            throw new NotFoundException("RESERVATION_PARAMETERS_NOT_FOUND");
        }
        LOG.debug("Reservation successfully updated");
        service.updateReservation(
                reservationId,
                reservationRequest.reservationDateTimeStart(),
                reservationRequest.reservationDateTimeEnd());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/reservation/{id}")
    public void deleteReservation(@PathVariable("id") Long reservationId) {
        if (reservationId == null) {
            throw new NotFoundException("RESERVATION_NOT_FOUND");
        }
        LOG.debug("Reservation {} successfully deleted", reservationId);
        service.deleteReservation(reservationId);
    }
}
