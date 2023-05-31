package reservation.room.meeting.sem.ear.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;
    
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;
    
    @Column(name = "date_of_create", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateCreates;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "payment")
    private Set<Reservation> reservations;
    
    @ManyToOne
    @JoinColumn(name = "user_payments")
    private User user;
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Set<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(Set<Reservation> reservations) { this.reservations = reservations; }
    
    public LocalDateTime getDateCreates() {
        return dateCreates;
    }
    
    public void setDateCreates(LocalDateTime dateCreates) {
        this.dateCreates = dateCreates;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "Payment{" + "id=" + id + ", totalPrice=" + totalPrice + ", dateCreates=" + dateCreates + '}';
    }
}
