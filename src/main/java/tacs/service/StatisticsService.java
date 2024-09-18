package tacs.service;

import org.springframework.stereotype.Service;
import tacs.dto.StatisticsResponse;
import tacs.dto.EventStatisticsResponse;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.User;
import tacs.repository.EventRepository;
import tacs.repository.TicketRepository;
import tacs.repository.UserRepository;
import tacs.statistics.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final EventRepository eventsRepository;
    private final TicketRepository ticketsRepository;
    private final UserRepository usersRepository;

    public StatisticsService(EventRepository eventsRepository, TicketRepository ticketsRepository,
                             UserRepository usersRepository) {
        this.eventsRepository = eventsRepository;
        this.ticketsRepository = ticketsRepository;
        this.usersRepository = usersRepository;
    }

    public List<StatisticsResponse> getStatistics(){
        List<StatisticsResponse> results = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfTheYear = LocalDateTime.of(now.getYear(), Month.JANUARY, 1, 0, 0);
        results.add(this.queryStatisticsBy(firstDayOfTheYear, now,"Yearly"));

        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).
                toLocalDate().atStartOfDay();
        results.add(this.queryStatisticsBy(startOfWeek, now,"Weekly"));

        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        results.add(this.queryStatisticsBy(startOfDay, now,"Daily"));
        return results;
    }

    private StatisticsResponse queryStatisticsBy(LocalDateTime fechaInicio, LocalDateTime fechaFin,
                                                 String rangoTemporal){
        StatisticsGenerator statisticsGenerator = new StatisticsGenerator();

        List<Event> events = this.eventsRepository.findByCreationDateBetween(fechaInicio,fechaFin);
        List<Ticket> tickets = this.ticketsRepository.findByReservationDateBetween(fechaInicio, fechaFin);
        List<User> users = this.usersRepository.findByLastLoginBetween(fechaInicio,fechaFin);

        EventStatistics eventStatis = new EventStatistics();
        TicketStatistics ticketStatistics = new TicketStatistics();
        UserStatistics userStatistics = new UserStatistics();

        statisticsGenerator.addStatistics(eventStatis);
        statisticsGenerator.addStatistics(ticketStatistics);
        statisticsGenerator.addStatistics(userStatistics);

        Map<String, List<?>> yearlyData = Map.of(
                "Tickets",tickets,
                "Events",events,
                "Users",users
        );

        StatisticsResults statistics =  statisticsGenerator.generateStatistics(yearlyData);
        StatisticsResponse response = new StatisticsResponse();
        Map<Statistics<?>, Integer> resultadosEstadisticas = statistics.getResults();

        response.setTimeRange(rangoTemporal);
        response.setEvents(resultadosEstadisticas.get(eventStatis));
        response.setTickets(resultadosEstadisticas.get(ticketStatistics));
        response.setLogins(resultadosEstadisticas.get(userStatistics));
        return response;
    }


    public Integer getTicketStatisticsById(LocalDateTime startDate, LocalDateTime endDate,
                                           String timeRange, int id) {
        StatisticsGenerator statisticsGenerator = new StatisticsGenerator();
        List<Ticket> tickets = this.ticketsRepository.findByReservationDateBetween(startDate, endDate);

        // Esto, por ahora propongo simularlo asi, dado que deberia generarse una consulta (dialecto SQL de hibernate)
        // en el repo, y no vamos a utilizar una BBDD de tipo relacional
        tickets = tickets.stream().filter(t -> t.getEvent().getId().equals(id)).collect(Collectors.toList());
        TicketStatistics ticketStatistics = new TicketStatistics();
        statisticsGenerator.addStatistics(ticketStatistics);

        Map<String, List<?>> yearlyData = Map.of(
                "Tickets", tickets
        );

        StatisticsResults statistics =  statisticsGenerator.generateStatistics(yearlyData);
        Map<Statistics<?>, Integer> results = statistics.getResults();
        return results.get(ticketStatistics);
    }

    public EventStatisticsResponse getStatisticsById(int id) {
        EventStatisticsResponse response = new EventStatisticsResponse();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfTheYear = LocalDateTime.of(now.getYear(), Month.JANUARY, 1, 0, 0);
        response.setYearlyTickets(this.getTicketStatisticsById(startOfTheYear, now,"Yearly",id));

        LocalDateTime startOfTheWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).
                toLocalDate().atStartOfDay();
        response.setWeeklyTickets(this.getTicketStatisticsById(startOfTheWeek, now,"Weekly",id));

        LocalDateTime startOfTheDay = now.toLocalDate().atStartOfDay();
        response.setDailyTickets(this.getTicketStatisticsById(startOfTheDay, now,"Daily",id));
        return response;
    }
}
