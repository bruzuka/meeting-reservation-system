package reservation.room.meeting.sem.ear.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

public record PaymentRequest(
        Long id,
        Long reservationId,
        
        Set<Long> reservationsId,
        
        Double totalPrice,
        
        @JsonProperty("date_of_create")
        LocalDateTime reservationDateTimeStart
) {
}
