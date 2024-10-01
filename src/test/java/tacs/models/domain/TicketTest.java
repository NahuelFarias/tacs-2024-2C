package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.TicketGenerator;
import tacs.models.domain.exception.PurchaseUnavailableException;
import tacs.models.domain.exception.SoldOutTicketsException;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertTrue;

public class TicketTest {

    private NormalUser testUser;
    private NormalUser testFakeUser;
    private Event testEvent;
    private Location testLocation;
    private TicketGenerator ticketGenerator;

    @BeforeEach
    public void setUp() {
        String username = "Pepe Rodriguez";
        this.testUser = new NormalUser(username);
        this.testFakeUser = new NormalUser("Pedro Pascal");

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

        this.ticketGenerator = new TicketGenerator(locations, mapaTickets);

        this.testEvent = new Event("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),ticketGenerator);
        this.testLocation = preferencia;
    }

    @Test
    public void reserveTicketsTest() {
        this.testUser.bookTicket(this.testEvent,this.testLocation, quantityTickets);
        Assertions.assertEquals(testUser.getTicketsOwned().size(),1);
    }

    @Test
    public void reserveTicketOnClosedSaleTest() {
        this.testEvent.closeSale();
        RuntimeException exception = Assertions.assertThrows(PurchaseUnavailableException.class,() -> {
            this.testUser.bookTicket(this.testEvent,this.testLocation, quantityTickets);
        });
        String expectedMessage = "Error code: Purchase Unavailable";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void reserveTicketOnOutSaleTest() {
        this.testUser.bookTicket(this.testEvent,this.testLocation, quantityTickets);
        RuntimeException exception = Assertions.assertThrows(SoldOutTicketsException.class,() -> {
            this.testFakeUser.bookTicket(this.testEvent,this.testLocation, quantityTickets);
        });
        String expectedMessage = "Error code: Sold out tickets";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getTicketsOwnedTest() {
        this.testUser.bookTicket(this.testEvent,this.testLocation, quantityTickets);
        List<Ticket> reservations = this.testUser.getTicketsOwned();
        Assertions.assertEquals(reservations.size(),1);
    }
}
