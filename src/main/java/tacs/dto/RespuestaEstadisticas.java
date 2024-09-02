package tacs.dto;

import lombok.Data;
import lombok.Setter;

@Data
public class RespuestaEstadisticas {

    @Setter
    private String rangoTemporal;
    @Setter
    private int cantidadTickets;
    @Setter
    private int cantidadEventos;
    @Setter
    private int cantidadLogins;
}
