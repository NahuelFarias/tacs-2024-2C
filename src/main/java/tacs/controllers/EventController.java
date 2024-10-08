package tacs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tacs.dto.CreateEvent;
import tacs.dto.CreateReservation;
import tacs.models.domain.events.Event;
import tacs.service.EventService;

import java.util.List;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/search")
    public Event getEventByName(@RequestParam("name") String name) {
        return eventService.getEventByName(name);
    }

    @GetMapping("/{id}/tickets")
    @ResponseBody
    public long getTicketsForSale(@PathVariable String id) {
        return eventService.getTicketsForSale(id);
    }

    @PatchMapping("/{id}/close")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void setStatusSales(@PathVariable String id) {
        eventService.setState(id, false);
    }

    @PostMapping
    public void createEvent(@RequestBody CreateEvent event) {
        eventService.createEvent(event);
    }

    @PostMapping("/{id}/reserves")
    public void createReserves(@PathVariable String id, @RequestParam("user_id") String userId, @RequestBody CreateReservation nameLocation) {
        eventService.createReserves(id, userId, nameLocation);
    }

    @GetMapping
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable String eventId) {
        return eventService.getEvent(eventId);
    }
}
