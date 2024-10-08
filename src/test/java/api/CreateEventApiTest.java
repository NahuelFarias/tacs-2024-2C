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
import tacs.dto.LocationDTO;
import tacs.models.domain.events.Event;
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
    private LocationDTO testLocation;

    private List<LocationDTO> testLocations;
    private Map<String, Integer> testTicketsMap;

    @BeforeEach
    public void setUp() {
        String username = "Pepe Rodriguez";
        this.testUser = new NormalUser(username);

        LocationDTO preferencia = new LocationDTO("Preferencia",500,12);
        LocationDTO eastStand = new LocationDTO("East Stand", 200, 20);
        LocationDTO tribunaNorte = new LocationDTO("Tribuna Norte", 400, 17);
        LocationDTO gradaSur = new LocationDTO("Grada Sur", 100, 100);

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
