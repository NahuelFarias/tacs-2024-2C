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
    @JsonProperty("open_sale")
    public boolean openSale;
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
        this.openSale = true;
    }

    @JsonIgnore
    public List<Ticket> getSoldTickets() {
        return this.tickets.stream().filter(Ticket::wasSold).collect(Collectors.toList());
    }

    public long getSoldTicketsAmount() {
        return this.getSoldTickets().size();
    }

    @JsonIgnore
    public List<Ticket> getAvailableTickets() {
        return this.tickets.stream().filter(t -> !t.wasSold()).collect(Collectors.toList());
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

    public void closeSale() {
        this.openSale = false;
    }

    public void updateSale(Boolean state) {
        this.openSale = state;
    }

    public Ticket makeReservation(Location location) {
        if(!this.openSale) throw new PurchaseUnavailableException();

        Ticket ticket = this.getAvailableTickets().stream()
            .filter(t -> t.getLocation().equals(location)).findFirst()
            .orElseThrow(SoldOutTicketsException::new);

        ticket.sell();

        return ticket;
    }

    public boolean purchaseAvailable() {
        return this.openSale;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getId() {
        return id;
    }
}
