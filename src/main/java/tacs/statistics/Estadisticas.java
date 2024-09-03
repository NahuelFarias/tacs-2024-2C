package tacs.statistics;

import java.util.List;

public interface Estadisticas<T> {
    Integer generarEstadistica(List<T> elementos);
    String name();
}
