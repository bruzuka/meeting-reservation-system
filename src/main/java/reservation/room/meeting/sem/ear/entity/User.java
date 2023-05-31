package reservation.room.meeting.sem.ear.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_profile")
public class User extends AbstractUser {

    @Enumerated(EnumType.STRING)
    private Role role;
    
    @ManyToMany
    @JoinTable(
            name = "user_has_reservation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "reservation_id")
    )
    private Set<Reservation> reservations;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Payment> payments;

    public User() {
        this.role = Role.USER;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    public Set<Payment> getPayments() {
        return payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public boolean isUser() {
        return role == Role.USER;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username=" + getUsername() +
                ", email=" + getEmail() +
                ", password=" + getPassword() +
                ", role=" + getRole() +
                '}';
    }
}
