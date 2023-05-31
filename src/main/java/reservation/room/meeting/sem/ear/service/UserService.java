package reservation.room.meeting.sem.ear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reservation.room.meeting.sem.ear.entity.Payment;
import reservation.room.meeting.sem.ear.entity.Reservation;
import reservation.room.meeting.sem.ear.entity.User;
import reservation.room.meeting.sem.ear.exception.InvalidFieldException;
import reservation.room.meeting.sem.ear.exception.MissingFieldException;
import reservation.room.meeting.sem.ear.repository.PaymentRepository;
import reservation.room.meeting.sem.ear.repository.ReservationRepository;
import reservation.room.meeting.sem.ear.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PaymentRepository paymentRepository,
                       ReservationRepository reservationRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User findUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(String username, String email, String password) {
        User newUser = new User();
        for (User user : userRepository.findAll()) {
            if (user.getEmail().equals(email)) {
                throw new InvalidFieldException("Email " + email + " is already taken.");
            }
            if (user.getUsername().equals(username)) {
                throw new InvalidFieldException("Username " + username + " is already taken.");
            }
        }
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.encodePassword(passwordEncoder); // hashing password
        return userRepository.save(newUser);
    }

    public void toUserAddReservation(Long userId, Long reservationId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new InvalidFieldException("User with Id " + userId + " does not exist.")
        );
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new InvalidFieldException("Reservation with id " + reservationId + " does not exist.")
        );
        user.getReservations().add(reservation);
        userRepository.save(user);
    }


    public void toUserAddPayment(Long userId, Long paymentId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new InvalidFieldException("User with Id " + userId + " does not exist.")
        );
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new InvalidFieldException("Reservation with id " + paymentId + " does not exist.")
        );
        payment.setUser(user);
        userRepository.save(user);
    }


    @Transactional
    public void updateUser(Long userId, String username, String email, String password) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new InvalidFieldException("User with Id " + userId + " does not exist.")
        );
        if (username != null && username.length() > 0
        && !Objects.equals(user.getUsername(), username)) {
            user.setUsername(username);
        } else {
            throw new MissingFieldException();
        }
        if (email != null && email.length() > 0
                && !Objects.equals(user.getEmail(), email)) {
            user.setEmail(email);
        } else {
            throw new MissingFieldException();
        }
        if (password != null && password.length() > 0
                && !Objects.equals(user.getPassword(), password)) {
            user.setPassword(password);
        } else {
            throw new MissingFieldException();
        }
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new InvalidFieldException("User with id " + userId + " does not exists.");
        }
        userRepository.deleteById(userId);
    }

    public Set<User> findAllUsersAsc() {
        return userRepository.findAllByIdIsNotNullOrderByUsernameAsc();
    }

    public Set<User> findAllUsersDesc() {
        return userRepository.findAllByIdIsNotNullOrderByUsernameDesc();
    }

    public User findUserByEmail(@Email String email) {
        User user = new User();
        if (!user.getEmail().equals(email)) {
            throw new InvalidFieldException("User with email " + email + " does not exist.");
        }
        return userRepository.findByEmail(email);
    }
}
