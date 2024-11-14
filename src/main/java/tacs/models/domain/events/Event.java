package tacs.models.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
@NoArgsConstructor
public class Event {

    @Getter
    @Id
    private String id;

    @Getter
    public String name;
    @Getter
    public LocalDateTime date;
    @Getter
    @JsonProperty("open_sale")
    public boolean openSale;
    @Getter
    public LocalDateTime creationDate;

    @Setter
    @Getter
    public List<Location> locations;
    @JsonProperty("image_url")
    @Getter
    public String imageUrl;

    public Event(String name, LocalDateTime date, String imageUrl) {
        this.name = name;
        this.date = date;
        this.creationDate = LocalDateTime.now();
        this.openSale = true;
        this.imageUrl = imageUrl;
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

    public List<Ticket> createTickets(String locationName, Integer quantityTickets) {
        Location location = this.locationByName(locationName);

        return location.makeReservation(this, quantityTickets);
    }

    public boolean purchaseAvailable() {
        return this.openSale;
    }

    public Location locationByName(String locationName){
        return this.locations.stream()
                .filter(l -> l.getName().equals(locationName))
                .findFirst()
                .orElse(null);
    }

}
