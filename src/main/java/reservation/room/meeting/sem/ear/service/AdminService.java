package reservation.room.meeting.sem.ear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reservation.room.meeting.sem.ear.entity.Admin;
import reservation.room.meeting.sem.ear.entity.Reservation;
import reservation.room.meeting.sem.ear.entity.Room;
import reservation.room.meeting.sem.ear.exception.InvalidFieldException;
import reservation.room.meeting.sem.ear.exception.LengthFieldException;
import reservation.room.meeting.sem.ear.exception.MissingFieldException;
import reservation.room.meeting.sem.ear.repository.*;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminService {

    private final AdminRepository repository;
    private final RoomRepository roomRepository;
    private final ReservationRepository resRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository repository,
                        RoomRepository roomRepository,
                        ReservationRepository resRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roomRepository = roomRepository;
        this.resRepository = resRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Iterable<Admin> getAllAdmins() {
        return repository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        if (id == null) {
            throw new InvalidFieldException("Admin with this id does not exist");
        }
        return repository.findById(id);
    }

    public Admin createAdmin(String username, String email, String password) {
        Admin admin = new Admin();
        for (Admin user : repository.findAll()) {
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                throw new MissingFieldException();
            }
            if (user.getEmail().equals(email)) {
                throw new InvalidFieldException("Email " + email + " is already taken.");
            }
            if (user.getUsername().equals(username)) {
                throw new InvalidFieldException("Username " + username + " is already taken.");
            }
            if (username.length() > 10L || password.length() < 8L) {
                throw new LengthFieldException();
            }
        }
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.encodePassword(passwordEncoder);
        return repository.save(admin);
    }

    public void createAdminReservation(Long id, Long reservationId) {
        Admin admin = repository.findById(id).orElseThrow(
                () -> new InvalidFieldException("Admin with id " + id + " does not exist.")
        );
        Reservation reservation = resRepository.findById(reservationId).orElseThrow(
                () -> new InvalidFieldException("Reservation with id " + reservationId + " does not exist.")
        );
        admin.setAdminReservations(reservation.getRoomReservation().getReservations());
        repository.save(admin);
    }

    
    public void toAdminAddRoom(Long adminId, Long roomId) {
        Admin admin = repository.findById(adminId).orElseThrow(
                () -> new InvalidFieldException("Admin with Id " + adminId + " does not exists.")
        );
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new InvalidFieldException("Reservation with id " + roomId + " does not exist.")
        );
        admin.getAdminRooms().add(room);
        repository.save(admin);
    }

    public Set<Admin> findAdminListAsc() {
        return repository.findAllByIdIsNotNullOrderByUsernameAsc();
    }

    public Set<Admin> findAdminListDesc() {
        return repository.findAllByIdIsNotNullOrderByUsernameDesc();
    }

    @Transactional
    public void updateAdmin(Long adminId, String email) {
        Admin admin = repository.findById(adminId).orElseThrow(
                () -> new InvalidFieldException("Admin with Id " + adminId + " does not exists.")
        );
        if (email != null && email.length() > 0 && !Objects.equals(admin.getEmail(), email)) {
            admin.setEmail(email);
        } else {
            throw new MissingFieldException();
        }
        repository.save(admin);
    }

    public void deleteAdmin(Long adminId) {
        boolean exists = repository.existsById(adminId);
        if (!exists) {
            throw new InvalidFieldException("Admin with id " + adminId + " does not exists.");
        }
        repository.deleteById(adminId);
    }
}
