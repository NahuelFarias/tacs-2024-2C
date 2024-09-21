package tacs.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import tacs.models.domain.events.Location;

@Data
public class CreateGenerator {

    private List<Location> locations;
    private Map<String,Integer> ticketsMap;
}
