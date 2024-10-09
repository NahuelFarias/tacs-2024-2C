package tacs.models.domain.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import tacs.models.domain.events.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@NoArgsConstructor
public class NormalUser {
    @Id
    private String id;
    @JsonProperty("username")
    public String username;
    public String hashedPassword;
    public String salt;
    @JsonProperty("tickets")
    private List<String> ticketIds; // Lista de IDs de tickets comprados
    public LocalDateTime lastLogin;
    public Rol rol;

    public NormalUser(String username) {
        this.username = username;
        this.ticketIds = new ArrayList<>();
        //Usuario normal por defecto
        this.rol = new Rol("ROLE_USER");
    }
    public void bookTicket(Ticket ticket) {
        this.addTicket(ticket.getId());
        ticket.changeOwner(this.getId());
        ticket.setReservationDate(LocalDateTime.now());
    }

    public void addTicket(String ticketId) {
        this.ticketIds.add(ticketId);
    }

    public List<String> getTicketsOwned() {
        return this.ticketIds;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public String getId() {
        return id;
    }

    public Rol getRol() {
        return rol;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
