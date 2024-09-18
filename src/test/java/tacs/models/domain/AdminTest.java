package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.TicketGenerator;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.Admin;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdminTest {

    private Admin testAdmin;
    private Event testEvent;
    private Location testLocation;

    @BeforeEach
    public void setUp() {
        String username = "Pepa Admin";
        this.testAdmin = new Admin(username);

        Location preferencia = new Location("Preferencia", 500);
        Location eastStand = new Location("East Stand", 200);
        Location tribunaNorte = new Location("Tribuna Norte", 400);
        Location gradaSur = new Location("Grada Sur", 100);

        List<Location> ubicaciones = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        Map<String, Integer> mapaTickets = Map.of(
                "Preferencia", 1,
                "East Stand", 11,
                "Tribuna Norte", 50,
                "Grada Sur", 23
        );

        TicketGenerator generator = new TicketGenerator(ubicaciones,mapaTickets);

        this.testEvent = new Event("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),generator);
        this.testLocation = preferencia;
    }

    @Test
    public void disableEventTest() {
        this.testAdmin.disableEvent(testEvent);
        Assertions.assertFalse(testEvent.purchaseAvailable());
    }

}
