package tacs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tacs.dto.CreateEvent;
import tacs.dto.CreateReservation;
import tacs.dto.LocationDTO;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.events.Ticket;
import tacs.repository.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    // Método que convierte LocationDTO a Location
    private List<Location> convertToLocations(List<LocationDTO> locationDTOs) {
        return locationDTOs.stream()
                .map(dto -> {
                    String locationId = UUID.randomUUID().toString();
                    Location location = new Location(dto.getName(), dto.getPrice(), dto.getQuantityTickets());
                    location.setId(locationId);
                    return location;
                })
                .collect(Collectors.toList());
    }

    public void createEvent(CreateEvent eventDTO) {
        Event event = new Event(eventDTO.getName(), eventDTO.getDate());
        List<Location> locations = this.convertToLocations(eventDTO.getLocations());
        event.setLocations(locations);
        eventRepository.save(event);
    }

    public Event getEvent(String id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    public Event getEventByName(String name) {
    Optional<Event> opcEvento = eventRepository.findByNormalizedName(name);
        if (opcEvento.isPresent()) {
            return opcEvento.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
    }

    public void setState(String id, Boolean state) {
        Event event = this.getEvent(id);
        event.updateSale(state);
        eventRepository.save(event);
    }

    public void createReserves(String id, String userId, CreateReservation createReservation) {
        Event event = this.getEvent(id);
        String locationName = createReservation.getName();
        Ticket ticket = event.makeReservation(locationName);
        userService.reserveTicket(userId, ticket);
        eventRepository.save(event);
    }

    public long getTicketsForSale(String id) {
        Event event = this.getEvent(id);
        return event.getAvailableTickets();
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

}
