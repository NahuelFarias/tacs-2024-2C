package tacs.models.domain.events;

import lombok.NoArgsConstructor;
import tacs.models.domain.exception.SoldOutTicketsException;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
@NoArgsConstructor
public class Location {

    @Id
    private String id;

    public String name;
    public double price;
    public long quantityTickets;
    public long quantityTicketsSold;

    public Location(String name, double price, long quantityTickets) {
        this.name = name;
        this.price = price;
        this.quantityTickets = quantityTickets;
        this.quantityTicketsSold = 0;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public long getQuantityTicketsSold() {
        return this.quantityTicketsSold;
    }

    public void setQuantityTickets(int quantityTickets) {
        this.quantityTickets = quantityTickets;
    }

    public double getQuantityTickets() {
        return quantityTickets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //TODO: Chequeo si tenemos tickets para vender
    public Ticket makeReservation(Event event) {
        if (this.quantityTickets <= 0) throw new SoldOutTicketsException();
        Ticket ticket = new Ticket(event.getId(), this.getId());
        this.quantityTicketsSold++;
        this.quantityTickets--;
        return ticket;
    }

    // Sobrescribir equals() para comparar objetos basados en los valores de sus atributos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        
        return Objects.equals(name, location.name) &&
                Objects.equals(price, location.price);
    }

    // Sobrescribir hashCode() para garantizar que objetos iguales tengan el mismo hash
    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
