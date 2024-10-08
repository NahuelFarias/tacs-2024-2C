package api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import tacs.App;
import tacs.dto.CreateEvent;
import tacs.dto.StatisticsResponse;
import tacs.dto.EventStatisticsResponse;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class StatisticsApiTest {
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

        Location preferencia = new Location("Preferencia",500,12);
        Location eastStand = new Location("East Stand", 200, 20);
        Location tribunaNorte = new Location("Tribuna Norte", 400, 17);
        Location gradaSur = new Location("Grada Sur", 100, 100);


        List<Location> locations = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        Map<String, Integer> ticketsMap = Map.of(
                "Preferencia", 1,
                "East Stand", 11,
                "Tribuna Norte", 50,
                "Grada Sur", 23
        );

        this.testLocation = preferencia;

        CreateEvent createEvent = new CreateEvent();
        createEvent.setDate(LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay());
        createEvent.setName("River vs Boca");
        // createEvent.setLocations(locations);

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

    @Test
    @Order(1)
    public void generateEventTest() {
        String url = "http://localhost:" + port + "/statistics/use";
        ResponseEntity<List<StatisticsResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StatisticsResponse>>() {}
        );
        List<StatisticsResponse> statistics = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, statistics.get(0).getEvents());
        System.out.println(statistics);


    }

    @Test
    @Order(2)
    public void getStatisticsByIdTest() {
        String url = "http://localhost:" + port + "/statistics/events/1/tickets";

        ResponseEntity<EventStatisticsResponse> response = restTemplate.getForEntity(url,
                EventStatisticsResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getDailyTickets());
    }

}
