package tacs.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tacs.models.domain.statistics.EstadisticaService;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    @GetMapping("/use")
    public String getUseStatistics() {
        return "todo bien loko";
    }

    @GetMapping("/events/{id}/tickets")
    public String getTicketsSales(@PathVariable Integer id) {
        return "todo bien loko";
    }
}