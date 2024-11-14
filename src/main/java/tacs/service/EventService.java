package tacs.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tacs.dto.CreateEvent;
import tacs.dto.CreateReservation;
import tacs.dto.LocationDTO;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.exception.SoldOutTicketsException;
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

    @Autowired
    MongoTemplate mongoTemplate;

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
        Event event = new Event(eventDTO.getName(), eventDTO.getDate(), eventDTO.getImageUrl());
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

    public Event bookAndUpdate(String eventId, String locationName, int ticketsToBook) {

        Query eventQuery = new Query(Criteria.where("_id").is(new ObjectId(eventId)).and("openSale").is(true));
        Event event = mongoTemplate.findOne(eventQuery, Event.class);

        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with ID " + eventId + " not found or sale is not open.");
        }

        boolean locationExists = event.getLocations().stream()
                .anyMatch(location -> location.getName().equals(locationName));

        if (!locationExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with name " + locationName + " not found in event with ID " + eventId + ".");
        }

        Query query = new Query(Criteria.where("_id").is(new ObjectId(eventId))
                .and("locations").elemMatch(Criteria.where("name").is(locationName).and("quantityTickets").gte(ticketsToBook)));

        Update update = new Update()
                .inc("locations.$.quantityTickets", -ticketsToBook)
                .inc("locations.$.quantityTicketsSold", ticketsToBook);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

        Event updatedEvent = mongoTemplate.findAndModify(query, update, options, Event.class);

        if (updatedEvent == null) {
            throw new SoldOutTicketsException("Not enough tickets available for location " + locationName + " in event with ID " + eventId + ".");
        }

        return updatedEvent;
    }

    public void setState(String id, Boolean state) {
        Event event = this.getEvent(id);
        event.updateSale(state);
        eventRepository.save(event);
    }

    public void createReserves(String id, String userId, CreateReservation createReservation) {
        Event event = this.bookAndUpdate(id, createReservation.getName(), createReservation.getQuantityTickets());
        List<Ticket> tickets = event.createTickets(createReservation.getName(), createReservation.getQuantityTickets());
        userService.makeReservation(userId, tickets);
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
