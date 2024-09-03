package api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import tacs.App;
import tacs.dto.CrearUsuario;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.Usuario;

import java.util.List;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioExistenteApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        CrearUsuario crearUsuario = new CrearUsuario();
        crearUsuario.setName("Pepita");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<CrearUsuario> requestEntity = new HttpEntity<>(crearUsuario, headers);

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
    public void consultarUsuarioTest() {
        String url = "http://localhost:" + port + "/users";
        ResponseEntity<List<Usuario>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Usuario>>() {}
        );
        List<Usuario> usuarios = response.getBody();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, usuarios.size());
    }

    @Test
    @Order(2)
    public void obtenerDatosUsuarioTest(){
        String url = "http://localhost:" + port + "/users/1";

        ResponseEntity<Usuario> response = restTemplate.getForEntity(url, Usuario.class);

        Usuario usuario = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Pepita", usuario.username);
    }

    @Test
    @Order(3)
    public void obtenerReservasTest(){
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
