package reservation.room.meeting.sem.ear.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reservation.room.meeting.sem.ear.dto.request.RoomRequest;
import reservation.room.meeting.sem.ear.entity.Room;
import reservation.room.meeting.sem.ear.exception.NotFoundException;
import reservation.room.meeting.sem.ear.service.RoomService;

import java.util.ArrayList;

@RestController
public class RoomController {

    private static final Logger LOG = LoggerFactory.getLogger(RoomController.class);
    private final RoomService service;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/see/all/rooms")
    public ResponseEntity<Iterable<Room>> seeAllRooms() {
        return ResponseEntity.ok(service.seeAllRooms());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/see/all/free/rooms")
    public ResponseEntity<Iterable<Room>> getAllFreeRooms() {
        return ResponseEntity.ok(service.findAvailableReservations());
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/room/name")
    public ResponseEntity<Room> getRoomByName(@RequestBody RoomRequest request) {
        if (request.name() == null) {
            throw new NotFoundException("ROOM_NOT_FOUND");
        }
        return ResponseEntity.ok(service.findRoomByName(request.name()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/free/rooms/between/time")
    public ResponseEntity<Iterable<Room>> getFreeRoomsBetweenTime(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(
                service.findAvailableReservationsInSpecificTime(
                        request.reservationDateTimeStart(),
                        request.reservationDateTimeEnd()
                )
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/top/rooms/{num}/asc")
    public ResponseEntity<ArrayList<Room>> getTopRoomsByNumAsc(@PathVariable Integer num) {
        return ResponseEntity.ok(service.findTopRoomsByNumAsc(num));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/top/rooms/{num}/desc")
    public ResponseEntity<ArrayList<Room>> getTopRoomsByNumDesc(@PathVariable Integer num) {
        return ResponseEntity.ok(service.findTopRoomsByNumDesc(num));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/create/room")
    public ResponseEntity<Room> createRoom(@RequestBody RoomRequest request) {
        if (request.name() == null ||
                request.pricePerHour() == null ||
                request.description() == null) {
            throw new NotFoundException("ROOM_PARAMETERS_NOT_FOUND");
        }
        LOG.debug("Room {} successfully created", request.name());
        return ResponseEntity.ok(
                service.createRoom(
                        request.name(),
                        request.pricePerHour(),
                        request.description()
                )
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/meeting/room/change/room/{id}")
    public void updateRoom(
            @PathVariable("id") Long roomId,
            @RequestBody RoomRequest request) {
        if (roomId == null) {
            throw new NotFoundException("ROOM_NOT_FOUND");
        }
        if (request.name() == null || request.pricePerHour() == null || request.description() == null) {
            throw new NotFoundException("ROOM_PARAMETERS_NOT_FOUND");
        }
        LOG.debug("Room successfully updated");
        service.updateRoom(roomId, request.name(), request.pricePerHour(), request.description());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/room/{id}")
    public void deleteRoom(@PathVariable("id") Long roomId) {
        if (roomId == null) {
            throw new NotFoundException("ROOM_NOT_FOUND");
        }
        LOG.debug("Room {} successfully deleted", roomId);
        service.deleteRoom(roomId);
    }
}