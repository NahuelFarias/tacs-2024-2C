package tacs.models.domain.users;

import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ticket;
import java.util.List;

public class Usuario {

    public String username;
    public List<Ticket> ticketsAsociados;

    public Evento buscarEvento(String nombreEvento){
        //TODO: Logica busqueda de eventos
        return new Evento(null, null, false);
    }

    public Usuario(String username) {
        this.username = username;
    }

    public void aniadirTicket(Ticket ticketNuevo) {
        ticketsAsociados.add(ticketNuevo);
    }
}
