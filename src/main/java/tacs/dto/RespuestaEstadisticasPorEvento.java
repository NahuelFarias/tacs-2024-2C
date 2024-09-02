package tacs.dto;

import lombok.Data;
import lombok.Setter;

@Data
public class RespuestaEstadisticasPorEvento {

    @Setter
    private int cantidadTicketsAnio;
    @Setter
    private int cantidadTicketsSemana;
    @Setter
    private int cantidadTicketsDia;
}
