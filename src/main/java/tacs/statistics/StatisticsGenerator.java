package tacs.statistics;

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
        for(Statistics statistics : this.statistics) {
            if (data.containsKey(statistics.name())) {
                statisticsResults.addStatistics(statistics, statistics.generateStatistics(data.get(statistics.name())));

            }
            else{
                throw new WrongStatisticsException();
            }
        }
        return this.statisticsResults;
    }
}
