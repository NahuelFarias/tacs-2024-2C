package tacs.statistics;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneradorEstadisticas {
    private List<Estadisticas> estadisticas;

    @Getter
    private ResultadosEstadisticas resultadosEstadisticas;

    public GeneradorEstadisticas() {
        this.estadisticas = new ArrayList<>();
        this.resultadosEstadisticas = new ResultadosEstadisticas();
    }

    public void agregarEstadistica(Estadisticas estadistica) {
        this.estadisticas.add(estadistica);
    }

    public ResultadosEstadisticas generarEstadisticas(Map<String,List<?>> datos){
        for(Estadisticas estadistica : this.estadisticas){
            resultadosEstadisticas.agregarEstadisticas(estadistica, estadistica.generarEstadistica(datos.get(estadistica.name())));
        }

        return this.resultadosEstadisticas;
    }
}
