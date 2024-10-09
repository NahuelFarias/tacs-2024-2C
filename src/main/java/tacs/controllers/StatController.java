package tacs.controllers;

import org.springframework.web.bind.annotation.*;

import tacs.dto.StatisticsResponse;
import tacs.dto.EventStatisticsResponse;
import tacs.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatController {

    private final StatisticsService statisticsService;

    public StatController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/use")
    @ResponseBody
    public List<StatisticsResponse> getUseStatistics() {
        return statisticsService.getStatistics();
    }

    @GetMapping("/events/{id}/tickets")
    @ResponseBody
    public EventStatisticsResponse getTicketsSales(@PathVariable String id) {
        return statisticsService.getStatisticsById(id);
    }
}
