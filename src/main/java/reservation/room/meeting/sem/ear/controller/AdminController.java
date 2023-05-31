package reservation.room.meeting.sem.ear.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reservation.room.meeting.sem.ear.dto.request.AdminRequest;
import reservation.room.meeting.sem.ear.entity.Admin;
import reservation.room.meeting.sem.ear.exception.NotFoundException;
import reservation.room.meeting.sem.ear.service.AdminService;

import java.util.Optional;
import java.util.Set;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/admins/list")
    public ResponseEntity<Iterable<Admin>> getAdminList() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/admin/{id}")
    public ResponseEntity<Optional<Admin>> getAdminId(@PathVariable Long id) {
        if (id == null) {
            throw new NotFoundException("ADMIN_NOT_FOUND");
        }
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/admin/list/asc")
    public ResponseEntity<Set<Admin>> getAdminListAsc() {
        return ResponseEntity.ok(adminService.findAdminListAsc());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/admin/list/desc")
    public ResponseEntity<Set<Admin>> getAdminListDesc() {
        return ResponseEntity.ok(adminService.findAdminListDesc());
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create/admin")
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminRequest adminRequest) {
        if (adminRequest.username() == null
                || adminRequest.email() == null
                || adminRequest.password() == null) {
            throw new NotFoundException("PARAMETERS_NOT_FOUND");
        }
        LOG.debug("Admin {} successfully created", adminRequest.username());
        return ResponseEntity.ok(
                adminService.createAdmin(adminRequest.username(), adminRequest.email(), adminRequest.password())
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/update/admin/{id}")
    public void updateAdmin(@PathVariable("id") Long adminId, @RequestBody AdminRequest adminRequest) {
        if (adminId == null || adminRequest.email() == null) {
            throw new NotFoundException("ADMIN_NOT_FOUND");
        }
        LOG.debug("Admin successfully updated");
        adminService.updateAdmin(adminId, adminRequest.email());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/admin/add/room")
    public void toAdminAddRoom(@RequestBody AdminRequest adminRequest) {
        if (adminRequest.id() == null) {
            throw new NotFoundException("ADMIN_NOT_FOUND");
        }
        if (adminRequest.roomId() == null) {
            throw new NotFoundException("ROOM_NOT_FOUND");
        }
        LOG.debug("Admin {} successfully added to room {}", adminRequest.username(), adminRequest.roomId());
        adminService.toAdminAddRoom(adminRequest.id(), adminRequest.roomId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/admin/has/reservation")
    public void adminSetReservation(@RequestBody AdminRequest adminRequest) {
        if (adminRequest.id() == null) {
            throw new NotFoundException("ADMIN_NOT_FOUND");
        }
        if (adminRequest.reservationId() == null) {
            throw new NotFoundException("RESERVATION_NOT_FOUND");
        }
        LOG.debug("Admin {} successfully set reservation {}", adminRequest.username(), adminRequest.reservationId());
        adminService.createAdminReservation(adminRequest.id(), adminRequest.reservationId());
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/delete/admin/{id}")
    public void deleteAdmin(@PathVariable("id") Long adminId) {
        if (adminId == null) {
            throw new NotFoundException("ADMIN_NOT_FOUND");
        }
        LOG.debug("Admin {} successfully deleted", adminId);
        adminService.deleteAdmin(adminId);
    }
}
