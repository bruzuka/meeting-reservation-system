package reservation.room.meeting.sem.ear.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "reservation_maker")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "reservation_date_time_start", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDateTimeStart;

    @Column(name = "reservation_date_time_end", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDateTimeEnd;

    @ManyToMany(mappedBy = "reservations")
    private Set<User> userReservation;

    @ManyToOne
    @JoinColumn(name = "reservations_payment")
    private Payment payment;

    @ManyToMany(mappedBy = "reservations")
    private Set<Admin> adminReservations;

    @ManyToOne
    @JoinColumn(name = "room_reservation")
    private Room roomReservation;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public LocalDateTime getReservationDateTimeStart() {
        return reservationDateTimeStart;
    }
    
    public void setReservationDateTimeStart(LocalDateTime reservationDateTimeStart) {
        this.reservationDateTimeStart = reservationDateTimeStart;
    }
    
    public LocalDateTime getReservationDateTimeEnd() {
        return reservationDateTimeEnd;
    }
    
    public void setReservationDateTimeEnd(LocalDateTime reservationDateTimeEnd) {
        this.reservationDateTimeEnd = reservationDateTimeEnd;
    }
    
    public Set<User> getUserReservation() {
        return userReservation;
    }
    
    public void setUserReservation(Set<User> userReservation) {
        this.userReservation = userReservation;
    }
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    public Set<Admin> getAdminReservations() {
        return adminReservations;
    }
    
    public void setAdminReservations(Set<Admin> adminReservations) {
        this.adminReservations = adminReservations;
    }
    
    public Room getRoomReservation() {
        return roomReservation;
    }
    
    public void setRoomReservation(Room roomReservation) {
        this.roomReservation = roomReservation;
    }
    
    @Override
    public String toString() {
        return "Reservation{" + "id=" + id +
                ", price=" + price +
                ", reservationDateTimeStart=" + reservationDateTimeStart +
                ", reservationDateTimeEnd=" + reservationDateTimeEnd +
                '}';
    }
}
