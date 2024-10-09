package tacs.models.domain.events;

import lombok.NoArgsConstructor;
import tacs.models.domain.exception.SoldOutTicketsException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
@NoArgsConstructor
public class Location {

    @Id
    private String id;

    public String name;
    public double price;
    public int quantityTickets;
    private int quantityTicketsSold;


    public Location(String name, double price, int quantityTickets) {
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

    public void setQuantityTickets(int quantityTickets) {
        this.quantityTickets = quantityTickets;
    }
    
    public void setQuantityTicketsSold(int quantityTicketsSold) {
        this.quantityTicketsSold = quantityTicketsSold;
    }

    public int getQuantityTickets() {
        return quantityTickets;
    }
    public int getQuantityTicketsSold() {
        return quantityTicketsSold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Ticket> makeReservation(Event event, Integer quantityTickets) {
        if (this.quantityTickets <= 0) throw new SoldOutTicketsException("No quedan suficientes tickets");
        List<Ticket> tickets = IntStream.range(0, quantityTickets)
                .mapToObj(i -> new Ticket(event.getId(), this.getId()))  // Crear una nueva instancia de Ticket
                .collect(Collectors.toList());
        this.setQuantityTicketsSold(this.getQuantityTicketsSold()+quantityTickets);
        this.setQuantityTickets(this.getQuantityTickets()-quantityTickets);
        return tickets;
    }

}
