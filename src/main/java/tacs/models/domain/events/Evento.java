package tacs.models.domain.events;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Evento {

    public String nombre;
    public LocalDateTime fecha;
    public List<Ubicacion> ubicaciones;
    public boolean ventaAbierta;
    public List<Ticket> tickets;

    public Evento(String nombre, LocalDateTime fecha, boolean ventaAbierta) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.ventaAbierta = ventaAbierta;
    }

    public int cantidadTicketsVendidos() {
        return (int) tickets.stream().filter(Ticket::fueUsado).count();
    }
    public double obtenerVentaTotal() {
        return this.tickets.stream().mapToDouble(Ticket::getPrecio).sum();
    }
}
