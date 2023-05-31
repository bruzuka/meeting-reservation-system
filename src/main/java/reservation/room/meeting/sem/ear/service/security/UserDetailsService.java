package reservation.room.meeting.sem.ear.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reservation.room.meeting.sem.ear.entity.User;
import reservation.room.meeting.sem.ear.repository.UserRepository;


/*
 * Resource: https://gitlab.fel.cvut.cz/ear/b221-eshop
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }
        return new reservation.room.meeting.sem.ear.security.model.UserDetails(user);
    }
}

