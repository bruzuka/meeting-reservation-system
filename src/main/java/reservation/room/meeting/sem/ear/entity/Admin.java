package reservation.room.meeting.sem.ear.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "admin_profile")
public class Admin extends AbstractUser {

    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToMany
    @JoinTable(
            name = "admin_control_room",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<Room> rooms;

    @ManyToMany
    @JoinTable(
            name = "admin_has_reservation",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "reservation_id")
    )
    private Set<Reservation> reservations;

    public Admin() {
        this.role = Role.ADMIN;
    }

    public Set<Room> getAdminRooms() {
        return rooms;
    }

    public void setAdminRooms(Set<Room> adminRooms) {
        this.rooms = adminRooms;
    }

    public Set<Reservation> getAdminReservations() {
        return reservations;
    }

    public void setAdminReservations(Set<Reservation> adminReservations) {
        this.reservations = adminReservations;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", username=" + getUsername() +
                ", email=" + getEmail() +
                ", password=" + getPassword() +
                '}';
    }
}
