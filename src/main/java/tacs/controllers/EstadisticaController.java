package tacs.controllers;

import org.springframework.web.bind.annotation.*;

import tacs.dto.RespuestaEstadisticas;
import tacs.dto.RespuestaEstadisticasPorEvento;
import tacs.service.EstadisticaService;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping("/use")
    @ResponseBody
    public List<RespuestaEstadisticas> getUseStatistics() {
        return estadisticaService.obtenerEstadisticas();
    }

    @GetMapping("/events/{id}/tickets")
    @ResponseBody
    public RespuestaEstadisticasPorEvento getTicketsSales(@PathVariable Integer id) {
        return estadisticaService.obtenerEstadisticaPorID(id);
    }
}
