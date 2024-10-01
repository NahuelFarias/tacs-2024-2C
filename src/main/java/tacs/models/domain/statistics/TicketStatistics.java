package tacs.models.domain.statistics;

import tacs.models.domain.events.Ticket;

import java.util.List;

public class TicketStatistics implements Statistics<Ticket> {
    @Override
    public Integer generateStatistics(List<Ticket> tickets) {
        return tickets.size();
    }

    @Override
    public String name() {
        return "Tickets";
    }
}
