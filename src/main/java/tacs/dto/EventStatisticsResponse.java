package tacs.dto;

import lombok.Data;

@Data
public class EventStatisticsResponse {

    private int yearlyTickets;
    private int weeklyTickets;
    private int dailyTickets;
}
