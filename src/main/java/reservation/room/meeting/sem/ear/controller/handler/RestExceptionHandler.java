package reservation.room.meeting.sem.ear.controller.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import reservation.room.meeting.sem.ear.dto.response.ErrorResponse;
import reservation.room.meeting.sem.ear.exception.InvalidFieldException;
import reservation.room.meeting.sem.ear.exception.LengthFieldException;
import reservation.room.meeting.sem.ear.exception.MissingFieldException;
import reservation.room.meeting.sem.ear.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MISSING_FIELD = "MISSING_FIELD_ERROR";
    public static final String INVALID_FIELD = "INVALID_FIELD_ERROR";
    public static final String LONG_FIELD = "LONG_FIELD_ERROR";

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<List<ErrorResponse>> wrongFormatException(ConstraintViolationException ex) {
        List<ErrorResponse> responses = new ArrayList<>();
        ex.getConstraintViolations().forEach(error ->
                responses.add(
                        new ErrorResponse(
                                error.getMessage(),
                                error.getInvalidValue().toString()
                        )
                )
        );
        return new ResponseEntity<>(responses, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {LengthFieldException.class})
    protected ResponseEntity<Object> longFieldException(LengthFieldException ex) {
        return badRequest(LONG_FIELD + ": " + ex.getMessage());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> notFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getErrorCode());
    }

    @ExceptionHandler(value = {InvalidFieldException.class})
    protected ResponseEntity<Object> invalidFieldException(InvalidFieldException ex) {
        return badRequest(INVALID_FIELD + ": " + ex.getMessage());
    }

    @ExceptionHandler(value = {MissingFieldException.class})
    protected ResponseEntity<Object> missingFieldException(MissingFieldException ex) {
        return badRequest(MISSING_FIELD + ": " + ex.getMessage());
    }

    private ResponseEntity<Object> badRequest(String msg) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }
}
