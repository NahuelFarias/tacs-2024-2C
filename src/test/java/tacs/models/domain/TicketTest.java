package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.exception.PurchaseUnavailableException;
import tacs.models.domain.exception.SoldOutTicketsException;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.assertions.Assertions.assertTrue;

public class TicketTest {

    private NormalUser testUser;
    private NormalUser testFakeUser;
    private Event testEvent;
    private Location testLocation;
    private Ticket testTicket;

    @BeforeEach
    public void setUp() {
        String username = "Pepe Rodriguez";
        this.testUser = new NormalUser(username);
        this.testFakeUser = new NormalUser("Pedro Pascal");

        Location preferencia = new Location("Preferencia",500,1);
        Location eastStand = new Location("East Stand", 200, 0);
        Location tribunaNorte = new Location("Tribuna Norte", 400, 0);
        Location gradaSur = new Location("Grada Sur", 100, 0);

        List<Location> locations = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));

        this.testEvent = new Event("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(), "");
        testEvent.setLocations(locations);
        this.testLocation = preferencia;

        this.testTicket = new Ticket(this.testEvent.getId(),this.testLocation.getId());
    }

  @Test
    public void reserveTicketsTest() {
        this.testUser.bookTicket(this.testTicket);
        Assertions.assertEquals(testUser.getTicketsOwned().size(),1);
    }

   @Test
    public void reserveTicketOnClosedSaleTest() {
        this.testEvent.closeSale();
        RuntimeException exception = Assertions.assertThrows(PurchaseUnavailableException.class,() -> {
            this.testEvent.createTickets("Preferencia",1);
        });
        String expectedMessage = "Error code: Purchase Unavailable";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void reserveTicketOnOutSaleTest() {
        RuntimeException exception = Assertions.assertThrows(SoldOutTicketsException.class,() -> {
            this.testEvent.createTickets("Preferencia",2);
        });
        String expectedMessage = "No quedan suficientes tickets";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getTicketsOwnedTest() {
        this.testUser.bookTicket(this.testTicket);
        List<String> reservationsID = this.testUser.getTicketsOwned();
        Assertions.assertEquals(reservationsID.size(),1);
    }
}
