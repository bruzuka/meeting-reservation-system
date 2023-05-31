package reservation.room.meeting.sem.ear.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reservation.room.meeting.sem.ear.Generator;
import reservation.room.meeting.sem.ear.entity.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
@SpringBootTest
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void saveUser() {
        final User testUser = Generator.generateUser();
        final User userResult = userRepository.save(testUser);
        final Long userId = userResult.getId();
        assertThat(userRepository.findById(userId).get()).isEqualTo(testUser);
    }
}
