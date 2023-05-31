package reservation.room.meeting.sem.ear.dto.request;

public record AdminRequest(
        Long id,
        
        Long roomId,

        Long reservationId,
        
        String username,
        String email,
        String password
) {
}
