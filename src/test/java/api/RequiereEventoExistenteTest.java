package api;

import junit.framework.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import tacs.App;
import tacs.dto.CrearEvento;
import tacs.dto.CrearGenerador;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ubicacion;
import tacs.models.domain.users.Usuario;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequiereEventoExistenteTest {

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
    public void getTicketsDisponiblesTest(){
        String url = "http://localhost:" + port + "/events/1/tickets";

        ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Long cantidadTickets = response.getBody();
        assertEquals(85, cantidadTickets);
    }

    @Test
    @Order(2)
    public void cerrarVentaTest() {
        Boolean estado = Boolean.FALSE;
        String url = "http://localhost:" + port + "/events/1/sales" + "?state=" + estado;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
