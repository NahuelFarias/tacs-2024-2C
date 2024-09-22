package tacs.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tacs.dto.CreateGenerator;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.TicketGenerator;
import tacs.models.domain.events.Location;
import tacs.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    public void createEvent(String name, LocalDateTime date, CreateGenerator createGenerator) {
        TicketGenerator generator = new TicketGenerator(createGenerator.getLocations(), createGenerator.getTicketsMap());
        Event event = new Event(name, date, generator);
        eventRepository.save(event);
    }

    public Event getEvents() {
        return eventRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    public Event getEvent(Integer id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @Transactional
    public void setState(Integer id, Boolean state) {
        Event event = this.getEvent(id);
        event.updateSale(state);
    }

    @Transactional
    public void createReserves(Integer id, Integer userId, Location location) {
        Event event = this.getEvent(id);
        userService.reserveTicket(userId, event, location);
    }

    public long getTicketsForSale(Integer id) {
        Event event = this.getEvent(id);
        return event.getAvailableTicketsAmount();
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

}
