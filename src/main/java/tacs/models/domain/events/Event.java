package tacs.models.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import tacs.models.domain.exception.PurchaseUnavailableException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    public List<Location> locations;

    public Event(String name, LocalDateTime date, List<Location> locations) {
        this.name = name;
        this.date = date;
        this.locations = locations;
        this.creationDate = LocalDateTime.now();
        this.openSale = true;
    }

    @JsonIgnore
    public List<Ticket> getSoldTickets() {
        return this.locations.stream().flatMap(location -> location.getTickets().stream())
                .collect(Collectors.toList());
    }

    public long getSoldTicketsAmount() {
        return this.getSoldTickets().size();
    }

    @JsonIgnore
    public long getAvailableTickets() {
        return (long) this.locations.stream().mapToDouble(Location::getQuantityTickets).sum();
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

        return location.makeReservation(this);
    }

    public boolean purchaseAvailable() {
        return this.openSale;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Location locationByName(String nameLocation){
        return this.locations.stream()
                .filter(l -> l.getName().equals(nameLocation))
                .findFirst()
                .orElse(null);
    }

    public Integer getId() {
        return id;
    }
}
