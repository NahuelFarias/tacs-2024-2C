package api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;
import tacs.App;
import tacs.dto.*;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;
import tacs.security.CustomPBKDF2PasswordEncoder;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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
    private JWT jwt;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        String username = "Pepe Rodriguez";
        this.testUser = new NormalUser(username);

        Location preferencia = new Location("Preferencia",500,12);
        Location eastStand = new Location("East Stand", 200, 20);
        Location tribunaNorte = new Location("Tribuna Norte", 400, 17);
        Location gradaSur = new Location("Grada Sur", 100, 100);


        List<Location> locations = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        this.testLocation = preferencia;

        CreateEvent createEvent = new CreateEvent();
        createEvent.setDate(LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay());
        createEvent.setName("River vs Boca 2");
        createEvent.setLocations(locations);

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

        String saltUrl = "http://localhost:" + port + "/login/salt";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(saltUrl)
                .queryParam("username", "admin");

        HttpHeaders saltHeaders = new HttpHeaders();
        saltHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> saltResponse = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        String salt = saltResponse.getBody();

        String loginUrl = "http://localhost:" + port + "/login";
        CustomPBKDF2PasswordEncoder encoder = new CustomPBKDF2PasswordEncoder();
        String password =  this.hashPassword("admin123123",salt);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword(password);

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        loginHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer ");

        HttpEntity<LoginRequest> loginEntity = new HttpEntity<>(loginRequest, loginHeaders);

        ResponseEntity<JWT> loginResponse = restTemplate.exchange(
                "http://localhost:" + port + "/login",
                HttpMethod.POST,
                loginEntity,
                JWT.class
        );

        this.jwt = loginResponse.getBody();
    }

    @Test
    @Order(1)
    public void generateEventTest() {
        String url = "http://localhost:" + port + "/statistics/use";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " +  this.jwt.token());

        HttpEntity<?> eventEntity = new HttpEntity<>(headers);

        ResponseEntity<List<StatisticsResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                eventEntity,
                new ParameterizedTypeReference<List<StatisticsResponse>>() {}
        );
        List<StatisticsResponse> statistics = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, statistics.get(0).getEvents());
        System.out.println(statistics);
    }

    @Test
    @Order(2)
    public void getStatisticsByIdTest() {
        String url = "http://localhost:" + port + "/statistics/events/5/tickets";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " +  this.jwt.token());

        HttpEntity<?> eventEntity = new HttpEntity<>(headers);

        ResponseEntity<EventStatisticsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                eventEntity,
                EventStatisticsResponse.class
        );

        EventStatisticsResponse statistics = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getDailyTickets());
    }


    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] saltBytes = Base64.getDecoder().decode(salt);

        int iterations = 65536;
        int keyLength = 256;

        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, keyLength);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        String hashBase64 = Base64.getEncoder().encodeToString(hash);

        return salt + ":" + hashBase64;
    }
}
