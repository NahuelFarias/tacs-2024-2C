package tacs.models.domain.users;

import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Ubicacion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Usuario {

    public String username;
    public List<Ticket> ticketsAsociados;

    public Evento buscarEvento(String nombreEvento) {
        List<Evento> eventosAsociados = this.ticketsAsociados.stream().map(t -> t.getEventoAsociado())
                .collect(Collectors.toList());
        return eventosAsociados.stream().filter(e -> e.getNombre() == nombreEvento)
                .collect(Collectors.toList()).get(0);
    }

    public Usuario(String username) {
        this.username = username;
        this.ticketsAsociados = new ArrayList<>();
    }

    public void resevarTicket(Evento evento, Ubicacion ubicacion) {
        Ticket ticketReservado = evento.realizarReserva(ubicacion);
        ticketReservado.ticketTomado();
        this.ticketsAsociados.add(ticketReservado);
    }

    public void aniadirTicket(Ticket ticketNuevo) {
        ticketsAsociados.add(ticketNuevo);
    }

    public List<Ticket> getTicketsAsociados() {
        return this.ticketsAsociados;
    }

}
