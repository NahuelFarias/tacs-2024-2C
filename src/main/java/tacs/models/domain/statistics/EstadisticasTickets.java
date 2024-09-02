package tacs.models.domain.statistics;

import tacs.models.domain.events.Ticket;

import java.util.List;

public class EstadisticasTickets implements Estadisticas<Ticket> {
    private static EstadisticasTickets instance;

    public static EstadisticasTickets getInstance() {
        if (instance == null) {
            instance = new EstadisticasTickets();
        }
        return instance;
    }
    @Override
    public Integer generarEstadistica(List<Ticket> tickets) {
        return tickets.stream().filter(Ticket::getFueUsado).toList().size();
    }

    @Override
    public String name() {
        return "Tickets";
    }
}
