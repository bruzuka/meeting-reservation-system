package reservation.room.meeting.sem.ear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reservation.room.meeting.sem.ear.entity.Reservation;
import reservation.room.meeting.sem.ear.entity.Room;
import reservation.room.meeting.sem.ear.exception.InvalidFieldException;
import reservation.room.meeting.sem.ear.exception.MissingFieldException;
import reservation.room.meeting.sem.ear.repository.ReservationRepository;
import reservation.room.meeting.sem.ear.repository.RoomRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.abs;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final RoomRepository roomRepository;

    @Autowired
    public ReservationService(ReservationRepository repository, RoomRepository roomRepository) {
        this.repository = repository;
        this.roomRepository = roomRepository;
    }

    /*
    • Reservation service for Admin
    */

    public Iterable<Reservation> getAllReservations() {
        Iterable<Reservation> reservations = repository.findAll();
        for (Reservation reservation : reservations) {
            addPrice(reservation);
        }
        return reservations;
    }

    public Optional<Reservation> getReservation(Long reservationId) {
        Reservation reservation = repository.findById(reservationId).orElseThrow(
                () -> new InvalidFieldException("Reservation with id " + reservationId + " does not exist."));
        addPrice(reservation);
        return Optional.of(reservation);
    }

    public Iterable<Reservation> getAllReservationsBetween(LocalDateTime timeStart, LocalDateTime timeEnd) {
        return repository.findReservationsByReservationDateTimeStartBetweenOrReservationDateTimeEndBetween(
                timeStart, timeEnd,
                timeStart, timeEnd
        );
    }

    public void updateReservation(Long reservationId, LocalDateTime reservationDateTimeStart, LocalDateTime reservationDateTimeEnd) {
        Reservation reservation = repository.findById(reservationId).orElseThrow(
                () -> new InvalidFieldException("Reservation with id " + reservationId + " does not exist.")
        );
        if (reservationDateTimeStart != null &&
                !Objects.equals(reservation.getReservationDateTimeStart(), reservationDateTimeStart) &&
                !reservationDateTimeStart.isBefore(LocalDateTime.now())) {
            reservation.setReservationDateTimeStart(reservationDateTimeStart);
        }
        if (reservationDateTimeEnd != null &&
                !Objects.equals(reservation.getReservationDateTimeEnd(), reservationDateTimeEnd) &&
                !reservationDateTimeEnd.isAfter(LocalDateTime.of(
                        LocalDateTime.now().getYear(),
                        LocalDateTime.now().getMonth(),
                        LocalDateTime.MAX.getDayOfMonth(),
                        23, 59))) {
            reservation.setReservationDateTimeEnd(reservationDateTimeEnd);
        }
        reservation.setReservationDateTimeStart(reservationDateTimeStart);
        reservation.setReservationDateTimeEnd(reservationDateTimeEnd);
        repository.save(reservation);
    }

    /*
     * Usage for both roles
     */
    public ArrayList<Reservation> findTopReservationsByNumAsc(Integer n) {
        if (n == null) {
            throw new InvalidFieldException("Top number should be specified.");
        }
        return getReservations(repository.findAllByIdIsNotNullOrderByPriceAsc(), n);
    }

    public ArrayList<Reservation> findTopReservationsByNumDesc(Integer n) {
        if (n == null) {
            throw new InvalidFieldException("Top number should be specified.");
        }
        return getReservations(repository.findAllByIdIsNotNullOrderByPriceDesc(), n);
    }

    private ArrayList<Reservation> getReservations(ArrayList<Reservation> repository, Integer n) {
        ArrayList<Reservation> printReservations = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            printReservations.add(repository.get(i));
        }
        return printReservations;
    }

    public Iterable<Reservation> getAllReservationsByDateTimeAfter(LocalDateTime timeStart) {
        if (timeStart == null) {
            throw new MissingFieldException();
        }
        return repository.findAllByReservationDateTimeStartAfter(timeStart);
    }

    public Iterable<Reservation> getAllReservationsByDateTimeBefore(LocalDateTime timeEnd) {
        if (timeEnd == null) {
            throw new MissingFieldException();
        }
        return repository.findAllByReservationDateTimeEndBefore(timeEnd);
    }

    public Reservation createReservation(LocalDateTime reservationDateTimeStart, LocalDateTime reservationDateTimeEnd) {
        Reservation reservation = new Reservation();
        if (reservationDateTimeStart.isBefore(LocalDateTime.now())) {
            throw new DateTimeException("Time you set is less than current time.");
        }
        if (reservationDateTimeEnd.isAfter(LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.MAX.getDayOfMonth(),
                23, 59))) {
            throw new DateTimeException("Time you set is more than current time.");
        }
        reservation.setPrice(0D);
        reservation.setReservationDateTimeStart(reservationDateTimeStart);
        reservation.setReservationDateTimeEnd(reservationDateTimeEnd);
        return repository.save(reservation);
    }

    /*
    • Reservation service for User
    */

    public void toReservationAddRoom(Long reservationId, Long roomId) {
        Reservation reservation = repository.findById(reservationId)
                .orElseThrow(
                        () -> new InvalidFieldException("Reservation with id " + reservationId + " does not exist."));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(
                        () -> new InvalidFieldException("Reservation with id " + roomId + " does not exist."));
        reservation.setRoomReservation(room);
        addPrice(reservation);
        repository.save(reservation);
    }

    public void addPrice(Reservation reservation) {
        Room room = reservation.getRoomReservation();
        if (room == null) {
            reservation.setPrice(0D);
        } else {
            double price;
            int deltaHour = reservation.getReservationDateTimeStart().getHour() -
                    reservation.getReservationDateTimeEnd().getHour();
            int deltaMinute = reservation.getReservationDateTimeEnd().getMinute() -
                    reservation.getReservationDateTimeStart().getMinute();
            if (deltaHour > 0) {
                deltaHour = 24 - (deltaHour);
            } else {
                deltaHour = abs(deltaHour);
            }
            int inMinutes = deltaHour * 60 + deltaMinute;
            price = (double) inMinutes / 60 * room.getPricePerHour();
            reservation.setPrice(price);
            repository.save(reservation);
        }
    }

    public void deleteReservation(Long reservationId) {
        boolean exists = repository.existsById(reservationId);
        if (!exists) {
            throw new InvalidFieldException("Reservation with id " + reservationId + " does not exist.");
        }
        repository.deleteById(reservationId);
    }
}
