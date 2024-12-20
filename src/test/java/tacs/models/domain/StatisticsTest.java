package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.exception.WrongStatisticsException;
import tacs.models.domain.statistics.*;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static junit.framework.Assert.assertTrue;

public class StatisticsTest {
    private NormalUser testUser;
    private Event testEvent;
    private Location testLocation;
    private UserStatistics userStatistics;
    private EventStatistics eventStatistics;
    private TicketStatistics ticketStatistics;
    private StatisticsGenerator statisticsGenerator;
    private StatisticsResults statisticsResults;
    private List<NormalUser> testUsers;
    private List<Event> testEvents;

    @BeforeEach
    public void setUp() {
        String username = "Pepe Rodriguez";
        this.testUser = new NormalUser(username);
        this.testUser.setLastLogin(LocalDateTime.now());

        Location preferencia = new Location("Preferencia",500,12);
        Location eastStand = new Location("East Stand", 200, 20);
        Location tribunaNorte = new Location("Tribuna Norte", 400, 17);
        Location gradaSur = new Location("Grada Sur", 100, 100);

        List<Location> locations = new ArrayList<>(Arrays.asList(preferencia, eastStand, tribunaNorte, gradaSur));

        this.testEvent = new Event("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(), "");
        testEvent.setLocations(locations);
        this.testLocation = preferencia;


        this.testUsers = new ArrayList<>();
        this.testUsers.add(this.testUser);

        this.testEvents = new ArrayList<>();
        this.testEvents.add(this.testEvent);
    }

    @Test
    public void userStatisticsTest() {
        this.statisticsGenerator = new StatisticsGenerator();
        this.userStatistics = new UserStatistics();
        Map<String, List<?>> loggedUsers = new HashMap<>();
        loggedUsers.put("Users", this.testUsers);

        this.statisticsGenerator.addStatistics(this.userStatistics);
        this.statisticsResults = this.statisticsGenerator.generateStatistics(loggedUsers);

        Assertions.assertEquals(this.statisticsResults.getResults().get(this.userStatistics), 1);
    }

    @Test
    public void eventStatisticsTest() {
        this.statisticsGenerator = new StatisticsGenerator();
        this.eventStatistics = new EventStatistics();
        Map<String, List<?>> events = new HashMap<>();
        events.put("Events", this.testEvents);

        this.statisticsGenerator.addStatistics(this.eventStatistics);
        this.statisticsResults = this.statisticsGenerator.generateStatistics(events);

        Assertions.assertEquals(this.statisticsResults.getResults().get(this.eventStatistics), 1);
    }

    @Test
    public void ticketStatisticsTest() {
//
//        this.statisticsGenerator = new StatisticsGenerator();
//        this.ticketStatistics = new TicketStatistics();
//
//        Ticket ticket = new Ticket(this.testEvent.getId(), this.testLocation.getId());
//
//        this.testUser.bookTickets(ticket);
//        Map<String, List<?>> soldTickets = new HashMap<>();
//
//        soldTickets.put("Tickets", this.testUser.getTicketsOwned());
//
//        this.statisticsGenerator.addStatistics(this.ticketStatistics);
//        this.statisticsResults = this.statisticsGenerator.generateStatistics(soldTickets);
//
//        Assertions.assertEquals(this.statisticsResults.getResults().get(this.ticketStatistics), 1);
    }

    @Test
    public void statisticsTest() {
//        this.statisticsGenerator = new StatisticsGenerator();
//        this.userStatistics = new UserStatistics();
//        this.eventStatistics = new EventStatistics();
//        this.ticketStatistics = new TicketStatistics();
//
//        Ticket ticket = new Ticket(this.testEvent.getId(), this.testLocation.getId());
//        this.testUser.bookTicket(ticket);
//
//        Map<String, List<?>> statisticsTestData = new HashMap<>();
//        statisticsTestData.put("Users", this.testUsers);
//        statisticsTestData.put("Events", this.testEvents);
//        statisticsTestData.put("Tickets", this.testUser.getTicketsOwned());
//        this.statisticsGenerator.addStatistics(this.userStatistics);
//        this.statisticsGenerator.addStatistics(this.eventStatistics);
//        this.statisticsGenerator.addStatistics(this.ticketStatistics);
//        this.statisticsResults = this.statisticsGenerator.generateStatistics(statisticsTestData);
//
//        Assertions.assertEquals(this.statisticsResults.getResults().values().stream().mapToInt(Integer::intValue).sum(), 3);
    }

    @Test
    public void fakeStatisticByMapTest() {
//
//        this.statisticsGenerator = new StatisticsGenerator();
//        this.ticketStatistics = new TicketStatistics();
//
//        Ticket ticket = new Ticket(this.testEvent.getId(), this.testLocation.getId());
//        this.testUser.bookTicket(ticket);
//
//        Map<String, List<?>> statisticFake = new HashMap<>();
//        statisticFake.put("Tickets locos", this.testUser.getTicketsOwned());
//        this.statisticsGenerator.addStatistics(this.ticketStatistics);
//
//        RuntimeException exception = Assertions.assertThrows(WrongStatisticsException.class,() -> {
//            this.statisticsResults = this.statisticsGenerator.generateStatistics(statisticFake);
//        });
//        String expectedMessage = "Missing Statistics: [Tickets]";
//        String actualMessage = exception.getMessage();
//        System.out.println(actualMessage);
//        assertTrue(actualMessage.contains(expectedMessage));
    } // Don't match the name with the Method name() in TicketSatistics

    @Test
    public void fakeStatisticByGeneratorListTest() {
//
//        this.statisticsGenerator = new StatisticsGenerator();
//        this.ticketStatistics = new TicketStatistics();
//
//        Ticket ticket = new Ticket(this.testEvent.getId(), this.testLocation.getId());
//        this.testUser.bookTicket(ticket);
//
//        Map<String, List<?>> statisticFake = new HashMap<>();
//        statisticFake.put("Tickets", this.testUser.getTicketsOwned());
//        this.statisticsResults = this.statisticsGenerator.generateStatistics(statisticFake);
//        assertTrue(this.statisticsResults.getResults().isEmpty());
    } // Don't exist TicketStatistics in the generator
}
