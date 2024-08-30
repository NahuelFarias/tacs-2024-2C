package tacs.models.domain.events;

import tacs.models.domain.exception.TicketsAgotadosException;
import tacs.models.domain.exception.VentaCerradaException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Evento {

    public String nombre;
    public LocalDateTime fecha;
    public boolean ventaAbierta;
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

        if(ticketsDisponibles.stream().count() > 0)
            return ticketsDisponibles.get(0);
        else throw new TicketsAgotadosException();
    }

    public boolean ventaAbierta() {
        return this.ventaAbierta;
    }
}
