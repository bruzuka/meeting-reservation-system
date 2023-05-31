package reservation.room.meeting.sem.ear.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reservation.room.meeting.sem.ear.dto.request.UserRequest;
import reservation.room.meeting.sem.ear.entity.User;
import reservation.room.meeting.sem.ear.exception.NotFoundException;
import reservation.room.meeting.sem.ear.service.UserService;

import java.util.Optional;
import java.util.Set;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(path = "/find/user/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable("id") Long userId) {
        if (userId == null) {
            throw new NotFoundException("USER_NOT_FOUND");
        }
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(path = "/find/username/{username}")
    public ResponseEntity<User> getUserByName(@PathVariable("username") String username) {
        if (username == null) {
            throw new NotFoundException("USER_NOT_FOUND");
        }
        return ResponseEntity.ok(userService.findUserByName(username));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/user/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) {
        if (email == null) {
            throw new NotFoundException("USER_NOT_FOUND");
        }
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/all/users/asc")
    public ResponseEntity<Set<User>> getAllUsersAsc() {
        return ResponseEntity.ok(userService.findAllUsersAsc());
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/all/users/desc")
    public ResponseEntity<Set<User>> getAllUsersDesc() {
        return ResponseEntity.ok(userService.findAllUsersDesc());
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        if (userRequest.username() == null || userRequest.email() == null || userRequest.password() == null) {
            throw new NotFoundException("USER_PARAMETERS_NOT_FOUND");
        }
        LOG.debug("User {} successfully created", userRequest.username());
        return ResponseEntity.ok(
                userService.createUser(
                        userRequest.username(),
                        userRequest.email(),
                        userRequest.password()
                )
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/to/user/add/payment")
    public void addPaymentToUser(@RequestBody UserRequest userRequest) {
        if (userRequest.id() == null) {
            throw new NotFoundException("USER_NOT_FOUND");
        }
        if (userRequest.paymentId() == null) {
            throw new NotFoundException("PAYMENT_NOT_FOUND");
        }
        LOG.debug("Payment {} successfully added to user {}", userRequest.paymentId(), userRequest.username());
        userService.toUserAddPayment(userRequest.id(), userRequest.paymentId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/user/has/reservation")
    public void createUserReservation(@RequestBody UserRequest userRequest) {
        if (userRequest.id() == null) {
            throw new NotFoundException("USER_NOT_FOUND");
        }
        if (userRequest.reservationId() == null) {
            throw new NotFoundException("RESERVATION_NOT_FOUND");
        }
        LOG.debug("User {} reservation created", userRequest.username());
        userService.toUserAddReservation(userRequest.id(), userRequest.reservationId());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/update/user/{id}")
    public void updateUser(
            @PathVariable("id") Long userId,
            @RequestBody UserRequest userRequest) {
        if (userId == null
                || userRequest.username() == null
                || userRequest.email() == null
                || userRequest.password() == null) {
            throw new NotFoundException("USER_PARAMETERS_NOT_FOUND");

        }
        LOG.debug("User successfully updated");
        userService.updateUser(userId, userRequest.username(), userRequest.email(), userRequest.password());
    }

    /*
    • Admin Controlling User
    */

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/admin/get/all/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/admin/delete/user/{id}")
    public void deleteUser(@PathVariable("id") Long userId) {
        if (userId == null) {
            throw new NotFoundException("USER_NOT_FOUND");
        }
        userService.deleteUser(userId);
    }
}