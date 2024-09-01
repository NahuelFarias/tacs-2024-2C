package tacs.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import tacs.models.domain.events.Ubicacion;

@Data
public class CrearGenerador {

    private List<Ubicacion> ubicaciones;
    private Map<String,Integer> mapaTickets;
}
