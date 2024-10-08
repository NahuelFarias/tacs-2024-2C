package tacs.models.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//TODO: cambiar a @data? para evitar overridear equals y hashcode y sacar boilerplate
@Entity
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    public String name;
    public double price;

    @Basic
    public int quantityTickets;
    private int quantityTicketsSold;

    @JsonIgnore
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    public List<Ticket> tickets = new ArrayList<>();


    public Location(String name, double price, int quantityTickets) {
        this.name = name;
        this.price = price;
        this.quantityTickets = quantityTickets;
        this.tickets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }
    public void setQuantityTicketsSold(int quantityTicketsSold) {
        this.quantityTicketsSold = quantityTicketsSold;
    }
    public void setQuantityTickets(int quantityTickets) {
        this.quantityTickets = quantityTickets;
    }

    public int getQuantityTickets() {
        return quantityTickets;
    }
    public int getQuantityTicketsSold() {
        return quantityTicketsSold;
    }

    // Sobrescribir equals() para comparar objetos basados en los valores de sus atributos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Verifica si ambos objetos tienen la misma referencia
        if (o == null || getClass() != o.getClass()) return false; // Verifica si el objeto comparado es null o de diferente clase

        Location location = (Location) o; // Realiza un casting del objeto comparado a la clase Location

        // Compara todos los atributos de la clase para determinar si son iguales
        return Objects.equals(name, location.name) &&
                Objects.equals(price, location.price);
    }

    // Sobrescribir hashCode() para garantizar que objetos iguales tengan el mismo hash
    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }


    public List<Ticket> makeReservation(Event event, Integer quantityTickets) {
        List<Ticket> tickets = IntStream.range(0, quantityTickets)
                .mapToObj(i -> new Ticket(event, this))  // Crear una nueva instancia de Ticket
                .collect(Collectors.toList());
        this.setQuantityTicketsSold(this.getQuantityTicketsSold()+quantityTickets);
        this.setQuantityTickets(this.getQuantityTickets()-quantityTickets);
        return tickets;
    }


}
