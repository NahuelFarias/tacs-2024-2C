package api;

import junit.framework.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import tacs.App;
import tacs.dto.CrearEvento;
import tacs.dto.CrearGenerador;
import tacs.dto.RespuestaEstadisticas;
import tacs.dto.RespuestaEstadisticasPorEvento;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ubicacion;
import tacs.models.domain.users.Usuario;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class EstadisticasApiTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Usuario usuarioTest;
    private Evento eventoTest;
    private Ubicacion ubicacionTest;

    @BeforeEach
    public void setUp() {
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

    @Test
    @Order(1)
    public void testGenerarEvento() {
        String url = "http://localhost:" + port + "/statistics/use";
        ResponseEntity<List<RespuestaEstadisticas>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RespuestaEstadisticas>>() {}
        );
        List<RespuestaEstadisticas> estadisticas = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, estadisticas.get(0).getCantidadEventos());
    }

    @Test
    @Order(2)
    public void testEndPointEstadisticasPorID() {
        String url = "http://localhost:" + port + "/statistics/events/1/tickets";

        ResponseEntity<RespuestaEstadisticasPorEvento> response = restTemplate.getForEntity(url,
                RespuestaEstadisticasPorEvento.class);

        RespuestaEstadisticasPorEvento estadisticas = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getCantidadTicketsDia());
    }
}
