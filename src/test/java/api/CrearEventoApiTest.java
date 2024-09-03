package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static junit.framework.Assert.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrearEventoApiTest {

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
        String nombreUsuario = "Pepe Rodriguez";
        this.usuarioTest = new Usuario(nombreUsuario);

        Ubicacion preferencia = new Ubicacion("Preferencia", 500);
        Ubicacion eastStand = new Ubicacion("East Stand", 200);
        Ubicacion tribunaNorte = new Ubicacion("Tribuna Norte", 400);
        Ubicacion gradaSur = new Ubicacion("Grada Sur", 100);

        this.ubicacionesTest = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        this.mapaTicketsTest = Map.of(
                "Preferencia", 1,
                "East Stand", 11,
                "Tribuna Norte", 50,
                "Grada Sur", 23
        );

        this.ubicacionTest = preferencia;
    }

    @Test
    public void crearEventoTest() {

        CrearGenerador generadorTickets = new CrearGenerador();
        generadorTickets.setMapaTickets(this.mapaTicketsTest);
        generadorTickets.setUbicaciones(this.ubicacionesTest);

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

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
