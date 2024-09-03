package tacs.models.domain.statistics;

import tacs.models.domain.events.Evento;

import java.util.List;

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
