package tacs.models.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    private boolean sold;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private NormalUser owner;
    @Column
    public LocalDateTime reservationDate;

    public Ticket(Event event, Location location) {
        this.event = event;
        this.location = location;
        this.sold = false;
    }

    public void changeOwner(NormalUser newOwner) {
        this.owner = newOwner;
    }

    public void sell() {
        this.sold = true;
    }

    public boolean wasSold() {
        return this.sold;
    }

    public Event getEvent() {
        return event;
    }

    public Location getLocation() {
        return location;
    }

    public double searchPrice() {
        return this.location.getPrice();
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

}
