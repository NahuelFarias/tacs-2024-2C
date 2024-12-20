package tacs.models.domain.statistics;

import java.util.List;

public interface Statistics<T> {
    Integer generateStatistics(List<T> elements);
    String name();
}
