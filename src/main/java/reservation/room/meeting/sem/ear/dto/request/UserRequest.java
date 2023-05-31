package reservation.room.meeting.sem.ear.dto.request;

public record UserRequest(

        Long id,

        Long reservationId,
        Long paymentId,
        String username,
        String email,
        String password) {
}
