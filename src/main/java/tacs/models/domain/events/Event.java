package tacs.models.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import tacs.models.domain.exception.PurchaseUnavailableException;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
@NoArgsConstructor
public class Event {

    @Id
    private String id;
    
    public String name;
    public LocalDateTime date;
    @JsonProperty("open_sale")
    public boolean openSale;
    public LocalDateTime creationDate;
    public List<Location> locations;

    public Event(String name, LocalDateTime date) {
        this.name = name;
        this.date = date;
        this.creationDate = LocalDateTime.now();
        this.openSale = true;
    }

    @JsonIgnore
    public long getSoldTickets() {
        return (long) this.locations.stream().mapToDouble(Location::getQuantityTicketsSold).sum();
    }

    @JsonIgnore
    public long getAvailableTickets() {
        return (long) this.locations.stream().mapToDouble(Location::getQuantityTickets).sum();
    }

    public String getName() {
        return name;
    }

    public void setLocations(List<Location> locations)  {
        this.locations = locations;
    }

    public void closeSale() {
        this.openSale = false;
    }

    public void updateSale(Boolean state) {
        this.openSale = state;
    }

    public Ticket makeReservation(String locationName) {
        if(!this.openSale) throw new PurchaseUnavailableException();
        Location location = this.locationByName(locationName);
        Ticket newTicket = location.makeReservation(this);
        return newTicket;
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

    public String getId() {
        return id;
    }
}
