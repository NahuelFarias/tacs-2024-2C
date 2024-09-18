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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    @JsonProperty("name")
    public String username;
    @JsonProperty("tickets")
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    public List<Ticket> ticketsOwned;
    @Column
    public LocalDateTime lastLogin;

    public User(String username) {
        this.username = username;
        this.ticketsOwned = new ArrayList<>();
    }

    public void reserveTicket(Event event, Location location) {
        Ticket ticket = event.reserveTicket(location);
        this.addTicket(ticket);
        ticket.changeOwner(this);
        ticket.setReservationDate(LocalDateTime.now());
    }

    public void addTicket(Ticket ticket) {
        this.ticketsOwned.add(ticket);
    }

    public List<Ticket> getOwnedTickets() {
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
}
