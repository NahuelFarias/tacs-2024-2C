package tacs.models.domain.statistics;

import lombok.Getter;
import tacs.models.domain.exception.WrongStatisticsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsGenerator {
    private List<Statistics> statistics;

    @Getter
    private StatisticsResults statisticsResults;

    public StatisticsGenerator() {
        this.statistics = new ArrayList<>();
        this.statisticsResults = new StatisticsResults();
    }

    public void addStatistics(Statistics statistics) {
        this.statistics.add(statistics);
    }

    public StatisticsResults generateStatistics(Map<String,List<?>> data){
        List<String> missingStatistics = new ArrayList<>();
        for(Statistics statistics : this.statistics) {
            if (data.containsKey(statistics.name())) {
                statisticsResults.addStatistics(statistics, statistics.generateStatistics(data.get(statistics.name())));

            }
            else{
                missingStatistics.add(statistics.name());
            }
        }
        if(!missingStatistics.isEmpty()){
            throw new WrongStatisticsException("Missing Statistics: " + missingStatistics);
        }
        return this.statisticsResults;
    }
}
