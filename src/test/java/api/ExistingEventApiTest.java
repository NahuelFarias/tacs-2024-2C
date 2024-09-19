package api;

import junit.framework.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import tacs.App;
import tacs.dto.CreateEvent;
import tacs.dto.CreateGenerator;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExistingEventApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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

        List<Location> ubicaciones = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        Map<String, Integer> mapaTickets = Map.of(
                "Preferencia", 1,
                "East Stand", 11,
                "Tribuna Norte", 50,
                "Grada Sur", 23
        );

        this.testLocation = preferencia;

        CreateGenerator ticketGenerator = new CreateGenerator();
        ticketGenerator.setTicketsMap(mapaTickets);
        ticketGenerator.setLocations(ubicaciones);

        CreateEvent crearEvento = new CreateEvent();
        crearEvento.setDate(LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay());
        crearEvento.setName("River vs Boca");
        crearEvento.setTicketGenerator(ticketGenerator);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<CreateEvent> requestEntity = new HttpEntity<>(crearEvento, headers);

        String url = "http://localhost:" + port + "/events";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    @Test
    @Order(1)
    public void getAvailableTicketsTest(){
        String url = "http://localhost:" + port + "/events/1/tickets";

        ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Long tickets = response.getBody();
        assertEquals(85, tickets);
    }

    @Test
    @Order(2)
    public void disablePurchaseTest() {
        Boolean state = Boolean.FALSE;
        String url = "http://localhost:" + port + "/events/1/sales" + "?state=" + state;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
