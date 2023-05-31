package reservation.room.meeting.sem.ear.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import reservation.room.meeting.sem.ear.security.model.AuthToken;
import reservation.room.meeting.sem.ear.security.model.UserDetails;

/*
 * Resource: https://gitlab.fel.cvut.cz/ear/b221-eshop
 */
public class SecurityUtil {

    public static AuthToken setCurrentUser(UserDetails userDetails) {
        final AuthToken token = new AuthToken(userDetails.getAuthorities(), userDetails);
        token.setAuthenticated(true);

        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
        return token;
    }
}
