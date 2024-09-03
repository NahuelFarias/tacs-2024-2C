package tacs.models.domain.statistics;

import tacs.models.domain.events.Ticket;

import java.util.List;
import java.util.stream.Stream;

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
