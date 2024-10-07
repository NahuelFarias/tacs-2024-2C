package tacs.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tacs.dto.CreateReservation;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    public void createEvent(String name, LocalDateTime date, List<Location> locations, String imageUrl) {
        Event event = new Event(name, date, locations, imageUrl);
        eventRepository.save(event);
    }

    public Event getEvent(Integer id) {
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

    @Transactional
    public void setState(Integer id, Boolean state) {
        Event event = this.getEvent(id);
        event.updateSale(state);
    }

    @Transactional
    public void createReserves(Integer id, Integer userId, CreateReservation createReservation) {
        Event event = this.getEvent(id);
        Location location1 = event.locationByName(createReservation.getName());
        userService.reserveTicket(userId, event, location1);
    }

    public long getTicketsForSale(Integer id) {
        Event event = this.getEvent(id);
        return event.getAvailableTickets();
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

}
