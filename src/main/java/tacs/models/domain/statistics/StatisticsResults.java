package tacs.models.domain.statistics;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class StatisticsResults {

    @Getter
    private Map<Statistics<?>, Integer> results;
    private LocalDateTime creationDate;

    public StatisticsResults(){
        this.results = new HashMap<>();
        this.creationDate = LocalDateTime.now();
    }

    public void addStatistics(Statistics statistics, Integer result){
        this.results.put(statistics, result);
    }
}
