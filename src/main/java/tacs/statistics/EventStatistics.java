package tacs.statistics;

import tacs.models.domain.events.Event;

import java.util.List;

public class EventStatistics implements Statistics<Event> {
    @Override
    public Integer generateStatistics(List<Event> events) {
        return events.size();
    }

    @Override
    public String name() {
        return "Events";
    }
}
