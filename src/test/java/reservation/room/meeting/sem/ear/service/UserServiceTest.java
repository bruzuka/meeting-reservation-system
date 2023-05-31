package reservation.room.meeting.sem.ear.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reservation.room.meeting.sem.ear.Generator;
import reservation.room.meeting.sem.ear.entity.User;
import reservation.room.meeting.sem.ear.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@SpringBootTest
class UserServiceTest {
    
    @Autowired
    private UserRepository userRepository;
    private UserService userService;
    
    @Test
    void getAllUsers() {
    }
    
    @Test
    void getUser() {
    }
    
    @Test
    void createUser() {
        final User ExpectedUser = Generator.generateUser();
        userService.createUser(ExpectedUser.getUsername(),
                ExpectedUser.getEmail(),
                ExpectedUser.getPassword()
        );
    
    }
    
    @Test
    void toUserAddPayment() {
    }
    
    @Test
    void updateUser() {
    }
    
    @Test
    void deleteUser() {
    }
}