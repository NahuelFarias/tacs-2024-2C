package tacs.models.domain.statistics;

import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ticket;

import java.util.List;
import java.util.stream.Stream;

public class EstadisticasEventos implements Estadisticas<Evento>{
    @Override
    public Integer generarEstadistica(List<Evento> eventos) {
        return eventos.size();
    }

    @Override
    public String name() {
        return "Eventos";
    }
}
