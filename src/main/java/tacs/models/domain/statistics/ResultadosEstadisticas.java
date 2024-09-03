package tacs.models.domain.statistics;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Getter
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
