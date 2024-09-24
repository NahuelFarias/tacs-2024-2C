package tacs.models.domain.statistics;

import tacs.models.domain.events.Ticket;

import java.util.List;

public class TicketStatistics implements Statistics<Ticket> {
    @Override
    public Integer generateStatistics(List<Ticket> tickets) {
        return tickets.stream().filter(Ticket::wasSold).toList().size();
    }

    @Override
    public String name() {
        return "Tickets";
    }
}
