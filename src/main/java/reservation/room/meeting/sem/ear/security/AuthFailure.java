package reservation.room.meeting.sem.ear.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import reservation.room.meeting.sem.ear.security.model.LogStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Resource: https://gitlab.fel.cvut.cz/ear/b221-eshop
 */
@Service
public class AuthFailure implements AuthenticationFailureHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthFailure.class);

    private final ObjectMapper mapper;

    @Autowired
    public AuthFailure(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException {
        LOG.debug("Login failed for user {}.", httpServletRequest.getParameter(AssertionErrorConstant.USERNAME_PARAM));
        final LogStatus status = new LogStatus(false, false, null, e.getMessage());
        mapper.writeValue(httpServletResponse.getOutputStream(), status);
    }
}
