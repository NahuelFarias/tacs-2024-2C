package tacs.statistics;

import tacs.models.domain.events.Ticket;

import java.util.List;

public class EstadisticasTickets implements Estadisticas<Ticket> {
    @Override
    public Integer generarEstadistica(List<Ticket> tickets) {
        return tickets.stream().filter(Ticket::fueUsado).toList().size();
    }

    @Override
    public String name() {
        return "Tickets";
    }
}
