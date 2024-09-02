package tacs.statistics;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResultadosEstadisticas {

    @Getter
    private Map<Estadisticas<?>, Integer> resultados;
    private LocalDateTime fechaCreacion;

    public ResultadosEstadisticas(){
        this.resultados = new HashMap<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    public void agregarEstadisticas(Estadisticas estadisticas,Integer resultadoEstadistica){
        this.resultados.put(estadisticas, resultadoEstadistica);
    }
}
