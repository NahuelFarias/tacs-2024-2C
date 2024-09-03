package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import tacs.App;
import tacs.dto.CrearEvento;
import tacs.dto.CrearGenerador;
import tacs.dto.CrearUsuario;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ubicacion;
import tacs.models.domain.users.Usuario;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ReservaEventosApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Usuario usuarioTest;
    private Evento eventoTest;
    private Ubicacion ubicacionTest;

    private List<Ubicacion> ubicacionesTest;
    private Map<String, Integer> mapaTicketsTest;

    @BeforeEach
    public void setUp() {
        this.crearEvento();
        this.crearUsuario();
    }

    public void crearEvento() {
        String nombreUsuario = "Pepe Rodriguez";
        this.usuarioTest = new Usuario(nombreUsuario);

        Ubicacion preferencia = new Ubicacion("Preferencia", 500);
        Ubicacion eastStand = new Ubicacion("East Stand", 200);
        Ubicacion tribunaNorte = new Ubicacion("Tribuna Norte", 400);
        Ubicacion gradaSur = new Ubicacion("Grada Sur", 100);

        List<Ubicacion> ubicaciones = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        Map<String, Integer> mapaTickets = Map.of(
                "Preferencia", 1,
                "East Stand", 11,
                "Tribuna Norte", 50,
                "Grada Sur", 23
        );

        this.ubicacionTest = preferencia;

        CrearGenerador generadorTickets = new CrearGenerador();
        generadorTickets.setMapaTickets(mapaTickets);
        generadorTickets.setUbicaciones(ubicaciones);

        CrearEvento crearEvento = new CrearEvento();
        crearEvento.setFecha(LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay());
        crearEvento.setName("River vs Boca");
        crearEvento.setGeneradorTickets(generadorTickets);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<CrearEvento> requestEntity = new HttpEntity<>(crearEvento, headers);

        String url = "http://localhost:" + port + "/events";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    private void crearUsuario() {
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
    public void reservarTicketTest() {
        Integer idUsuario = 1;
        String url = "http://localhost:" + port + "/events/1/reserves" + "?user_id=" + idUsuario;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Ubicacion> requestEntity = new HttpEntity<>(this.ubicacionTest, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
