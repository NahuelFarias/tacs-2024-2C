package api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import tacs.App;
import tacs.dto.CrearUsuario;

import static junit.framework.Assert.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrarUsuarioApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registrarUsuarioTest() {
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
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
