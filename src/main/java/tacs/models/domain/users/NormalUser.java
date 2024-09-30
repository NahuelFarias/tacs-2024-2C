package tacs.models.domain.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@NoArgsConstructor
public class NormalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    @JsonProperty("username")
    public String username;
    @Basic
    public String hashedPassword;
    @Basic
    public String salt;
    @JsonProperty("tickets")
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    public List<Ticket> ticketsOwned;
    @Column
    public LocalDateTime lastLogin;
    @OneToOne(cascade = CascadeType.ALL)
    public Rol rol;

    public NormalUser(String username) {
        this.username = username;
        this.ticketsOwned = new ArrayList<>();
        //Usuario normal por defecto
        this.rol = new Rol("ROLE_USER");
    }

    public void bookTicket(Event event, Location location) {
        Ticket ticket = event.makeReservation(location);
        this.addTicket(ticket);
        ticket.changeOwner(this);
        ticket.setReservationDate(LocalDateTime.now());
    }

    public void addTicket(Ticket ticket) {
        this.ticketsOwned.add(ticket);
    }

    public List<Ticket> getTicketsOwned() {
        return this.ticketsOwned;
    }

    public Optional<Event> searchEvent(String eventName) {
        List<Event> eventsWithTicketsOwned = this.ticketsOwned.stream().map(Ticket::getEvent)
                .toList();
        return eventsWithTicketsOwned.stream().filter(e -> e.getName().equals(eventName))
                .findFirst();
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public Integer getId() {
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
