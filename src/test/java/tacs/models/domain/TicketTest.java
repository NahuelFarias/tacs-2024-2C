package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.TicketGenerator;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class TicketTest {

    private NormalUser testUser;
    private Event testEvent;
    private Location testLocation;

    @BeforeEach
    public void setUp() {
        String username = "Pepe Rodriguez";
        this.testUser = new NormalUser(username);

        Location preferencia = new Location("Preferencia", 500);
        Location eastStand = new Location("East Stand", 200);
        Location tribunaNorte = new Location("Tribuna Norte", 400);
        Location gradaSur = new Location("Grada Sur", 100);

        List<Location> locations = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        Map<String, Integer> mapaTickets = Map.of(
                "Preferencia", 1,
                "East Stand", 11,
                "Tribuna Norte", 50,
                "Grada Sur", 23
        );

        TicketGenerator generador = new TicketGenerator(locations, mapaTickets);

        this.testEvent = new Event("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),generador);
        this.testLocation = preferencia;
    }

    @Test
    public void reserveTicketsTest() {
        this.testUser.bookTicket(this.testEvent,this.testLocation);
        Assertions.assertEquals(testUser.getTicketsOwned().size(),1);
    }

    @Test
    public void getTicketsOwnedTest() {
        this.testUser.bookTicket(this.testEvent,this.testLocation);
        List<Ticket> reservations = this.testUser.getTicketsOwned();
        Assertions.assertEquals(reservations.size(),1);
    }
}
