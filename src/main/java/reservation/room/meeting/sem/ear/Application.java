package reservation.room.meeting.sem.ear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) // Spring without security
@SpringBootApplication //  Spring with security
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
