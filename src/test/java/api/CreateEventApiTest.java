package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import tacs.App;
import tacs.dto.CreateEvent;
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
public class CreateEventApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private NormalUser testUser;
    private Event testEvent;
    private Location testLocation;

    private List<Location> testLocations;
    private Map<String, Integer> testTicketsMap;

    @BeforeEach
    public void setUp() {
        String username = "Pepe Rodriguez";
        this.testUser = new NormalUser(username);

        Location preferencia = new Location("Preferencia",500,12);
        Location eastStand = new Location("East Stand", 200, 20);
        Location tribunaNorte = new Location("Tribuna Norte", 400, 17);
        Location gradaSur = new Location("Grada Sur", 100, 100);

        this.testLocations = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        this.testLocation = preferencia;
    }

    @Test
    public void createEventTest() {
        CreateEvent createEvent = new CreateEvent();
        createEvent.setDate(LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay());
        createEvent.setName("River vs Boca");
        createEvent.setLocations(this.testLocations);

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
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void createEventWithInvalidDateTest() {
        // Crear un JSON hardcodeado con datos inválidos
        String invalidJson = "{ \"date\": \"invalid-date\", \"name\": \"\", \"ticketGenerator\": { \"ticketsMap\": {}, \"locations\": [] } }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(invalidJson, headers);

        String url = "http://localhost:" + port + "/events";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Verificar que se devuelva un código de error 400 (Bad Request)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
