package reservation.room.meeting.sem.ear;

import reservation.room.meeting.sem.ear.entity.User;

import java.util.Random;

public class Generator {
    
    private static final Random RAND = new Random();
    
    public static int randomInt() {
        return RAND.nextInt();
    }
    
    public static int randomInt(int max) {
        return RAND.nextInt(max);
    }
    
    public static int randomInt(int min, int max) {
        assert min >= 0;
        assert min < max;
        
        int result;
        do {
            result = randomInt(max);
        } while (result < min);
        return result;
    }
    
    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }
    
    public static User generateUser() {
        final User user = new User();
        user.setUsername("username" + randomInt(1000));
        user.setEmail(randomInt(1000) + "@mail.com");
        user.setPassword(Integer.toString(randomInt(1000)));
        
        return user;
    }
    
}

