package tacs.models.domain.statistics;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneradorEstadisticas {
    private List<Estadisticas<Object>> estadisticas;

    @Getter
    private ResultadosEstadisticas resultadosEstadisticas;
    private static GeneradorEstadisticas instance;

    public static GeneradorEstadisticas getInstance() {
        if (instance == null) {
            instance = new GeneradorEstadisticas();
        }
        return instance;
    }
    public GeneradorEstadisticas() {
        this.estadisticas = new ArrayList<>();
//        this.resultadosRanking = new ResultadosRanking();
    }

    public void agregarEstadistica(Estadisticas<Object> estadistica) {
        this.estadisticas.add(estadistica);
    }

    public ResultadosEstadisticas generarEstadisticas(Map<String,List<Object>> datos){
        for(Estadisticas<Object> estadistica : this.estadisticas){
            resultadosEstadisticas.agregarEstadisticas(estadistica, estadistica.generarEstadistica(datos.get(estadistica.name())));
        }

        return this.resultadosEstadisticas;
    }
}
