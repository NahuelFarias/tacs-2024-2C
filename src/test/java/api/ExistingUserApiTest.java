package api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import tacs.App;
import tacs.dto.CreateUser;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.NormalUser;

import java.util.List;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExistingUserApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        CreateUser createUser = new CreateUser();
        createUser.setUsername("Pepita");
        createUser.setPassword("password123test");

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
    @Order(1)
    public void getUserTest() {
        String url = "http://localhost:" + port + "/users";
        ResponseEntity<List<NormalUser>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<NormalUser>>() {}
        );
        List<NormalUser> users = response.getBody();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(4, users.size());
    }

    @Test
    @Order(2)
    public void getUserDataTest(){
        String url = "http://localhost:" + port + "/users/4";

        ResponseEntity<NormalUser> response = restTemplate.getForEntity(url, NormalUser.class);

        NormalUser user = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Pepita", user.username);
    }

    @Test
    @Order(3)
    public void getReservationsTest(){
        String url = "http://localhost:" + port + "/users/1/reserves";

        ResponseEntity<List<Ticket>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Ticket>>() {}
        );
        List<Ticket> tickets = response.getBody();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(0, tickets.size());
    }
}
