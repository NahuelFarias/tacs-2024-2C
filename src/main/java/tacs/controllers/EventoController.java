package tacs.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tacs.models.domain.events.EventoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PutMapping("/{id}/sales")
    @ResponseBody
    public String setStatusSales(@PathVariable Integer id, @RequestParam String state) {
        return "ID: " + id + " Name: " + state;
    }

    @PostMapping
    public String createEvent(@RequestBody String entity) {
        return "HOLA";
    }

    @PostMapping("/{id}/reserves")
    public String createReserves(@RequestBody String entity) {
        return "HOLA";
    }

}
