package tacs.models.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import tacs.models.domain.exception.TicketsAgotadosException;
import tacs.models.domain.exception.VentaCerradaException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    public String nombre;
    public LocalDateTime fecha;
    @Column
    public LocalDateTime fechaCreacion;
    @JsonProperty("venta_abierta")
    public boolean ventaAbierta;
    @OneToMany(mappedBy = "eventoAsociado", cascade = CascadeType.ALL)
    public List<Ticket> tickets;

    public Evento(String nombre, LocalDateTime fecha, GeneradorTickets generador) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.fechaCreacion = LocalDateTime.now();
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

    public double obtenerVentaTotal() {
        return this.getTicketsVendidos().stream().mapToDouble(Ticket::buscarPrecio).sum();
    }

    public String getNombre() {
        return nombre;
    }

    public void cerrarVenta() {
        this.ventaAbierta = false;
    }

    public Ticket realizarReserva(Ubicacion ubicacion) {
        if(!this.ventaAbierta)
            throw new VentaCerradaException();

        List<Ticket> ticketsDisponibles = this.getTicketsDisponibles()
                .stream().filter(t -> t.getUbicacion().equals(ubicacion)).collect(Collectors.toList());

        if(ticketsDisponibles.stream().count() > 0) {
            Ticket ticketReservado = ticketsDisponibles.get(0);
            ticketReservado.consumite();
            return ticketReservado;
        }
        else throw new TicketsAgotadosException();
    }

    public boolean ventaAbierta() {
        return this.ventaAbierta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Integer getId() {
        return id;
    }
}
