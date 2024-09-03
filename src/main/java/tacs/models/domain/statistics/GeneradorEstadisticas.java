package tacs.models.domain.statistics;

import lombok.Getter;
import tacs.models.domain.exception.EstadisticasIncorrectasException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneradorEstadisticas {
    private List<Estadisticas<Object>> estadisticas;

    @Getter
    private ResultadosEstadisticas resultadosEstadisticas;
/*    private static GeneradorEstadisticas instance;

    public static GeneradorEstadisticas getInstance() {
        if (instance == null) {
            instance = new GeneradorEstadisticas();
        }
        return instance;
    }*/
    public GeneradorEstadisticas() {
        this.estadisticas = new ArrayList<>();
        this.resultadosEstadisticas = new ResultadosEstadisticas();
    }

    public void agregarEstadistica(Estadisticas estadistica) {
        this.estadisticas.add(estadistica);
    }

    public ResultadosEstadisticas generarEstadisticas(Map<String,List<?>> datos){
        for(Estadisticas estadistica : this.estadisticas){
            if(datos.containsKey(estadistica.name())){
                resultadosEstadisticas.agregarEstadisticas(estadistica, estadistica.generarEstadistica(datos.get(estadistica.name())));
            }
            else{
                System.out.println(estadistica.name());
                throw new EstadisticasIncorrectasException();
            }
        }

        return this.resultadosEstadisticas;
    }
}
