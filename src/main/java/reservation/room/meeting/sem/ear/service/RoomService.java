package reservation.room.meeting.sem.ear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reservation.room.meeting.sem.ear.entity.Room;
import reservation.room.meeting.sem.ear.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class RoomService {

    private final RoomRepository repository;

    @Autowired
    public RoomService(RoomRepository repository) {
        
        this.repository = repository;
    }

    public Iterable<Room> seeAllRooms() {
        return repository.findAll();
    }

    public Room createRoom(String name, Double pricePerHour, String description) {
        Room newRoom = new Room();
        for (Room room: repository.findAll()) {
            if(room.getName().equals(name)) {
                throw new IllegalStateException("Room with name " + name + " is already taken.");
            }
        }
        newRoom.setName(name);
        newRoom.setPricePerHour(pricePerHour);
        newRoom.setDescription(description);
        newRoom.setDateCreates(LocalDateTime.now());
        return repository.save(newRoom);
    }

    public void updateRoom(Long roomId, String name, Double pricePerHour, String description) {
        Room room = repository.findById(roomId).orElseThrow(
                () -> new IllegalStateException("Room with id " + roomId + " does not exist.")
        );
        if (name != null && !Objects.equals(room.getName(),name)) {
            room.setName(name);
        }
        if (name != null) {
            room.setPricePerHour(pricePerHour);
        }
        if (description != null && description.length() <= 20L) {
            room.setDescription(description);
        }
        repository.save(room);
    }


    public void deleteRoom(Long roomId) {
        boolean exists = repository.existsById(roomId);
        if (!exists) {
            throw new IllegalStateException("Room with id " + roomId + " does not exist.");
        }
        repository.deleteById(roomId);
    }
}
