package tacs.models.domain.events;

import jakarta.persistence.*;
import tacs.models.domain.exception.TicketsAgotadosException;
import tacs.models.domain.exception.VentaCerradaException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @Column
    public LocalDateTime fechaCreacion;
    @JsonIgnore
    @OneToMany(mappedBy = "eventoAsociado", cascade = CascadeType.ALL)
    public List<Ticket> tickets;

    public Evento(String nombre, LocalDateTime fecha, GeneradorTickets generador) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.tickets = generador.generar(this);
        this.fechaCreacion = LocalDateTime.now();
        this.ventaAbierta = true;
    }

    @JsonIgnore
    public List<Ticket> getTicketsVendidos() {
        return this.tickets.stream().filter(Ticket::vendido).collect(Collectors.toList());
    }

    public long getCantidadTicketsVendidos() {
        return this.getTicketsVendidos().size();
    }

    @JsonIgnore
    public List<Ticket> getTicketsDisponibles() {
        return this.tickets.stream().filter(t -> !t.vendido()).collect(Collectors.toList());
    }

    public long getCantidadTicketsDisponibles() {
        return this.getTicketsDisponibles().size();
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
        if(!this.ventaAbierta) throw new VentaCerradaException();

        Optional<Ticket> opcTicket = this.getTicketsDisponibles().stream()
            .filter(t -> t.getUbicacion().equals(ubicacion)).findFirst();

        if(!opcTicket.isPresent()) throw new TicketsAgotadosException();

        Ticket ticketReservado = opcTicket.get();

        ticketReservado.seVendio();

        return ticketReservado;
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
