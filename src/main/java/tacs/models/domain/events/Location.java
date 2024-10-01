package tacs.models.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import tacs.models.domain.exception.SoldOutTicketsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void setQuantityTickets(int quantityTickets) {
        this.quantityTickets = quantityTickets;
    }

    public double getQuantityTickets() {
        return quantityTickets;
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


    public Ticket makeReservation(Event event) {
        if (this.quantityTickets <= 0)
            throw new SoldOutTicketsException();

        Ticket ticket = new Ticket(event, this);
        this.tickets.add(ticket);
        this.quantityTickets--;
        return ticket;
    }
}
