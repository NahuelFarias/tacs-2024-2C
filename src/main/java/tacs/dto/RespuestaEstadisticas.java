package tacs.dto;

import lombok.Data;

@Data
public class RespuestaEstadisticas {

    private String rangoTemporal;
    private int cantidadTickets;
    private int cantidadEventos;
    private int cantidadLogins;
}
