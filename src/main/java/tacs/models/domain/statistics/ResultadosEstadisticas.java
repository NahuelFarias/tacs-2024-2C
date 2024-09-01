package tacs.models.domain.statistics;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultadosEstadisticas {

    private Map<Estadisticas, Integer> resultados;
    private LocalDateTime fechaCreacion;

    public ResultadosEstadisticas(){
        this.resultados = new HashMap<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    public void agregarEstadisticas(Estadisticas estadisticas,Integer resultadoEstadistica){
        this.resultados.put(estadisticas, resultadoEstadistica);
    }
}
