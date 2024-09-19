package tacs.models.domain.events;

import jakarta.persistence.*;
import tacs.models.domain.exception.SoldOutTicketsException;
import tacs.models.domain.exception.PurchaseUnavailableException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    public String name;
    public LocalDateTime date;
    @JsonProperty("purchase_available")
    public boolean purchaseAvailable;
    @Column
    public LocalDateTime creationDate;
    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    public List<Ticket> tickets;

    public Event(String name, LocalDateTime date, TicketGenerator generator) {
        this.name = name;
        this.date = date;
        this.tickets = generator.generate(this);
        this.creationDate = LocalDateTime.now();
        this.purchaseAvailable = true;
    }

    @JsonIgnore
    public List<Ticket> getSoldTickets() {
        return this.tickets.stream().filter(Ticket::isSold).collect(Collectors.toList());
    }

    public long getSoldTicketsAmount() {
        return this.getSoldTickets().size();
    }

    @JsonIgnore
    public List<Ticket> getAvailableTickets() {
        return this.tickets.stream().filter(t -> !t.isSold()).collect(Collectors.toList());
    }

    public long getAvailableTicketsAmount() {
        return this.getAvailableTickets().size();
    }

    public double getTotalSales() {
        return this.getSoldTickets().stream().mapToDouble(Ticket::searchPrice).sum();
    }

    public String getName() {
        return name;
    }

    public void disablePurchase() {
        this.purchaseAvailable = false;
    }

    public void setPurchaseAvailability(Boolean state) {
        this.purchaseAvailable = state;
    }

    public Ticket reserveTicket(Location location) {
        if(!this.purchaseAvailable) throw new PurchaseUnavailableException();

        Ticket ticket = this.getAvailableTickets().stream()
            .filter(t -> t.getLocation().equals(location)).findFirst()
            .orElseThrow(SoldOutTicketsException::new);

        ticket.sell();

        return ticket;
    }

    public boolean purchaseAvailable() {
        return this.purchaseAvailable;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getId() {
        return id;
    }
}
