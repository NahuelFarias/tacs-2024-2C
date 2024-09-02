package tacs.statistics;

import tacs.models.domain.events.Ticket;

import java.util.List;
import java.util.stream.Stream;

public interface Estadisticas<T> {
    Integer generarEstadistica(List<T> elementos);
    String name();
}
