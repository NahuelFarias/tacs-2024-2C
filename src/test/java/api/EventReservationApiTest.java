package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import tacs.App;
import tacs.dto.CreateEvent;
import tacs.dto.CreateGenerator;
import tacs.dto.CreateUser;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class EventReservationApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private NormalUser testUSer;
    private Event testEvent;
    private Location testLocation;

    private List<Location> testLocations;
    private Map<String, Integer> testTicketsMap;

    @BeforeEach
    public void setUp() {
        this.createEvent();
        this.createUser();
    }

    public void createEvent() {
        String username = "Pepe Rodriguez";
        this.testUSer = new NormalUser(username);

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

        this.testLocation = preferencia;

        CreateGenerator generadorTickets = new CreateGenerator();
        generadorTickets.setTicketsMap(mapaTickets);
        generadorTickets.setLocations(ubicaciones);

        CreateEvent createEvent = new CreateEvent();
        createEvent.setDate(LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay());
        createEvent.setName("River vs Boca");
        createEvent.setTicketGenerator(generadorTickets);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateEvent> requestEntity = new HttpEntity<>(createEvent, headers);

        String url = "http://localhost:" + port + "/events";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    private void createUser() {
        CreateUser createUser = new CreateUser();
        createUser.setUsername("Pepita");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateUser> requestEntity = new HttpEntity<>(createUser, headers);

        String url = "http://localhost:" + port + "/users";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    @Test
    @Disabled
    public void reserveTicketTest() {
        Integer userId = 1;
        String url = "http://localhost:" + port + "/events/1/reserves" + "?user_id=" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Location> requestEntity = new HttpEntity<>(this.testLocation, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}