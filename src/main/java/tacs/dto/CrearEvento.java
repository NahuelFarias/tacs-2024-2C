package tacs.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CrearEvento {

    private String name;
    private LocalDateTime fecha;
    private CrearGenerador generadorTickets;
}

