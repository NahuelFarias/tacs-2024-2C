package tacs.models.domain.events;

import tacs.models.domain.exception.TicketsAgotadosException;
import tacs.models.domain.exception.VentaCerradaException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    public String nombre;
    public LocalDateTime fecha;
    @JsonProperty("venta_abierta")
    public boolean ventaAbierta;
    @OneToMany(mappedBy = "eventoAsociado", cascade = CascadeType.ALL)
    public List<Ticket> tickets;

    public Evento(String nombre, LocalDateTime fecha, GeneradorTickets generador) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.tickets = generador.generar(this);
        this.ventaAbierta = true;
    }

    public List<Ticket> getTicketsVendidos() {
        return tickets.stream().filter(Ticket::fueUsado).collect(Collectors.toList());
    }

    public long getCantidadTicketsVendidos() {
        return this.getTicketsVendidos().stream().count();
    }

    public List<Ticket> getTicketsDisponibles() {
        return this.tickets.stream().filter(t -> !t.fueUsado()).collect(Collectors.toList());
    }

    public long getCantidadTicketsDisponibles() {
        return this.getTicketsDisponibles().stream().count();
    }

    public double obtenerVentaTotal() {
        return this.getTicketsVendidos().stream().mapToDouble(Ticket::buscarPrecio).sum();
    }

    public String getNombre() {
        return nombre;
    }

    public void cerrarVenta() {
        this.ventaAbierta = false;
    }

    public void modificarVenta(Boolean state) {
        this.ventaAbierta = state;
    }

    public Ticket realizarReserva(Ubicacion ubicacion) {
        if(!this.ventaAbierta)
            throw new VentaCerradaException();

        List<Ticket> ticketsDisponibles = this.getTicketsDisponibles()
                .stream().filter(t -> t.getUbicacion().equals(ubicacion)).collect(Collectors.toList());

        if(ticketsDisponibles.stream().count() > 0)
        //TODO: Descontar de los tickets disponibles
            return ticketsDisponibles.get(0);
        else throw new TicketsAgotadosException();
    }

    public boolean ventaAbierta() {
        return this.ventaAbierta;
    }
}
