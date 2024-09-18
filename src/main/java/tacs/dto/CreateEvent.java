package tacs.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreateEvent {

    private String name;
    private LocalDateTime date;
    private CreateGenerator ticketGenerator;
}

