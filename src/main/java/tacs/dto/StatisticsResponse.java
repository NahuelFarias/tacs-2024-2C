package tacs.dto;

import lombok.Data;

@Data
public class StatisticsResponse {

    private String timeRange;
    private int tickets;
    private int events;
    private int logins;
}
