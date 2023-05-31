package reservation.room.meeting.sem.ear.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reservation.room.meeting.sem.ear.entity.Room;

import java.util.ArrayList;
import java.util.Set;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    Set<Room> findRoomsByReservationsIsNull();

    @SuppressWarnings("NullableProblems")
    Set<Room> findAll();

    Room findByName(String name);

    ArrayList<Room> findAllByIdIsNotNullOrderByPricePerHourAsc();

    ArrayList<Room> findAllByIdIsNotNullOrderByPricePerHourDesc();
}