package tacs.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tacs.dto.CreateEvent;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.events.Event;
import tacs.service.EventService;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}/tickets")
    @ResponseBody
    public long getTicketsForSale(@PathVariable Integer id) {
        return eventService.getTicketsForSale(id);
    }

    @PutMapping("/{id}/sales")
    @ResponseBody
    public void setStatusSales(@PathVariable Integer id, @RequestParam Boolean state) {
        eventService.setState(id, state);
    }

    @PostMapping
    public void createEvent(@RequestBody CreateEvent event) {
        eventService.createEvent(event.getName(), event.getDate(), event.getTicketGenerator());
    }

    @PostMapping("/{id}/reserves")
    public void createReserves(@PathVariable Integer id, @RequestParam("user_id") Integer userId, @RequestBody Location location) {
        eventService.createReserves(id, userId, location);
    }

    @GetMapping
    public List<Event> getEvents() {
        return eventService.getEvents();
    }
}
