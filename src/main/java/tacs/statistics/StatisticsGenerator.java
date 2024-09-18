package tacs.statistics;

import lombok.Getter;
import tacs.models.domain.exception.WrongStatisticsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsGenerator {
    private List<Statistics> statistics;

    @Getter
    private StatisticsResults results;

    public StatisticsGenerator() {
        this.statistics = new ArrayList<>();
        this.results = new StatisticsResults();
    }

    public void addStatistics(Statistics statistics) {
        this.statistics.add(statistics);
    }

    public StatisticsResults generateStatistics(Map<String,List<?>> data){
        for(Statistics statistics : this.statistics) {
            if (data.containsKey(statistics.name())) {
                results.addStatistics(statistics, statistics.generateStatistics(data.get(statistics.name())));

            }
            else{
                throw new WrongStatisticsException();
            }
        }
        return this.results;
    }
}
