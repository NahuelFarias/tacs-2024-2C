package tacs.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import tacs.dto.StatisticsResponse;
import tacs.dto.EventStatisticsResponse;
import tacs.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@SecurityRequirement(name = "bearer-jwt")
public class StatController {

    private final StatisticsService statisticsService;

    public StatController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/use")
    @ResponseBody
    @Operation(summary = "Get usage statistics", description = "Requires ADMIN role")
    public List<StatisticsResponse> getUseStatistics() {
        return statisticsService.getStatistics();
    }

    @GetMapping("/events/{id}/tickets")
    @ResponseBody
    @Operation(summary = "Get ticket sales by event", description = "Requires ADMIN role")
    public EventStatisticsResponse getTicketsSales(@PathVariable String id) {
        return statisticsService.getStatisticsById(id);
    }
}
