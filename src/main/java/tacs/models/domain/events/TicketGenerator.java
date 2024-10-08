package tacs.models.domain.events;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketGenerator {

    public List<Location> locations;
    public Map<String,Integer> ticketsMap;
    public Event event;

    public TicketGenerator(List<Location> locations, Map<String, Integer> ticketsMap) {
        this.locations = locations;
        this.ticketsMap = ticketsMap;
    }

    // public List<Ticket> generate(Event event) {
    //     return this.ticketsMap.entrySet().stream()
    //             .flatMap(entry -> IntStream.range(0, entry.getValue())
    //                     .mapToObj(i -> new Ticket(event, searchLocation(entry.getKey()))))
    //             .collect(Collectors.toList());
    // }

/*
    public List<Ticket> generate(Event event) {
        return this.ticketsMap.entrySet().stream()
                .flatMap(entry -> IntStream.range(0, entry.getValue())
                        .mapToObj(i -> new Ticket(event, searchLocation(entry.getKey()))))
                .collect(Collectors.toList());
    }
*/

    private Location searchLocation(String locationName) {
        return this.locations.stream().filter(u -> u.getName().equals(locationName)).
                toList().get(0);
    }
}
