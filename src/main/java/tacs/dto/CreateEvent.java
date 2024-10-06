package tacs.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateEvent {

    private String name;
    private LocalDateTime date;
    private List<LocationDTO> locations;
}

