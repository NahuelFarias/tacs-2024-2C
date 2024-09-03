package tacs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tacs.dto.CrearEvento;
import tacs.models.domain.events.Ubicacion;
import tacs.service.EventoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping("/{id}/tickets")
    @ResponseBody
    public long getTicketsForSale(@PathVariable Integer id) {
        return eventoService.getTicketsForSale(id);
    }

    @PutMapping("/{id}/sales")
    @ResponseBody
    public void setStatusSales(@PathVariable Integer id, @RequestParam Boolean state) {
        eventoService.setState(id, state);
    }

    @PostMapping
    public void createEvent(@RequestBody CrearEvento evento) {
        eventoService.createEvent(evento.getName(), evento.getFecha(), evento.getGeneradorTickets());
    }

    @PostMapping("/{id}/reserves")
    public void createReserves(@PathVariable Integer id, @RequestParam("user_id") Integer userId, @RequestBody Ubicacion ubicacion) {
        eventoService.createReserves(id, userId, ubicacion);
    }

}
