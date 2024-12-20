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
import tacs.dto.LocationDTO;
import tacs.models.domain.events.Event;
import tacs.models.domain.users.NormalUser;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private LocationDTO testLocation;

    @BeforeEach
    public void setUp() {
        String username = "Pepe Rodriguez";
        this.testUser = new NormalUser(username);

        LocationDTO preferencia = new LocationDTO("Preferencia",500,12);
        LocationDTO eastStand = new LocationDTO("East Stand", 200, 20);
        LocationDTO tribunaNorte = new LocationDTO("Tribuna Norte", 400, 17);
        LocationDTO gradaSur = new LocationDTO("Grada Sur", 100, 100);


        List<LocationDTO> ubicaciones = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));

        this.testLocation = preferencia;

        CreateEvent crearEvento = new CreateEvent();
        crearEvento.setDate(LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay());
        crearEvento.setName("River vs Boca");
        crearEvento.setLocations(ubicaciones);
        crearEvento.setImageUrl("https://www.unidiversidad.com.ar/cache/bc764704c45badb463645914de89d182_1000_1100.jpg");

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
        assertEquals(149, tickets);
    }

    @Test
    @Order(2)
    @Disabled
    public void disablePurchaseTest() {
        Boolean state = Boolean.FALSE;
        String url = "http://localhost:" + port + "/events/1/sales" + "?state=" + state;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void bookTicketTest() {
        Integer userId = 4;
        String url = "http://localhost:" + port + "/events/1/reserves" + "?user_id=" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LocationDTO> requestEntity = new HttpEntity<>(this.testLocation, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
