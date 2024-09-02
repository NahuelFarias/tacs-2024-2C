package tacs.models.domain.statistics;

import tacs.models.domain.events.Evento;

import java.util.List;

public class EstadisticasEventos implements Estadisticas<Evento>{

    private static EstadisticasEventos instance;

    public static EstadisticasEventos getInstance() {
        if (instance == null) {
            instance = new EstadisticasEventos();
        }
        return instance;
    }
    @Override
    public Integer generarEstadistica(List<Evento> eventos) {
        return eventos.size();
    }

    @Override
    public String name() {
        return "Eventos";
    }
}
