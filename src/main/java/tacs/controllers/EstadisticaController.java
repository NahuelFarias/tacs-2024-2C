package tacs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacs.models.domain.statistics.GeneradorEstadisticas;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class EstadisticaController {

    private final GeneradorEstadisticas estadisticaService;

    @GetMapping("/use")
    public String getUseStatistics() {
        return "todo bien loko";
    }

    @GetMapping("/events/{id}/tickets")
    public String getTicketsSales(@PathVariable Integer id) {
        return "todo bien loko";
    }
}
