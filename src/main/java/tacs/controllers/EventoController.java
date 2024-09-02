package tacs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventoController {

    //private final EventoService eventoService;

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
