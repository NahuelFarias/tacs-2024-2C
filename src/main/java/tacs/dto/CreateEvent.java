package tacs.dto;

import lombok.Data;
import tacs.models.domain.events.Location;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateEvent {

    private String name;
    private LocalDateTime date;
    private List<Location> locations;
}

