package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.GeneradorTickets;
import tacs.models.domain.events.Ubicacion;
import tacs.models.domain.users.Usuario;
import tacs.statistics.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class TestEstadisticas {
    private Usuario usuarioTest;
    private Evento eventoTest;
    private Ubicacion ubicacionTest;
    private EstadisticasUsuarios estadisticasUsuarios;
    private EstadisticasEventos estadisticasEventos;
    private EstadisticasTickets estadisticasTickets;
    private GeneradorEstadisticas generadorEstadisticas;
    private ResultadosEstadisticas resultadosEstadisticas;
    private List<Usuario> usuariosTest;
    private List<Evento> eventosTest;

    @BeforeEach
    public void setUp() {
        String nombreUsuario = "Pepe Rodriguez";
        this.usuarioTest = new Usuario(nombreUsuario);
        this.usuarioTest.setUltimoLogin(LocalDateTime.now());
        Ubicacion preferencia = new Ubicacion("Preferencia", 500);
        Ubicacion eastStand = new Ubicacion("East Stand", 200);
        Ubicacion tribunaNorte = new Ubicacion("Tribuna Norte", 400);
        Ubicacion gradaSur = new Ubicacion("Grada Sur", 100);

        List<Ubicacion> ubicaciones = new ArrayList<>(Arrays.asList(preferencia, eastStand, tribunaNorte, gradaSur));
        Map<String, Integer> mapaTickets = Map.of(
                "Preferencia", 1,
                "East Stand", 11,
                "Tribuna Norte", 50,
                "Grada Sur", 23
        );

        GeneradorTickets generador = new GeneradorTickets(ubicaciones, mapaTickets);

        this.eventoTest = new Evento("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(), generador);
        this.ubicacionTest = preferencia;


        this.usuariosTest = new ArrayList<>();
        this.usuariosTest.add(this.usuarioTest);

        this.eventosTest = new ArrayList<>();
        this.eventosTest.add(this.eventoTest);
    }

    @Test
    public void estadisticaUsuarioTest() {
        this.generadorEstadisticas = new GeneradorEstadisticas();
        this.estadisticasUsuarios = new EstadisticasUsuarios();
        Map<String, List<?>> usuariosLogueados = new HashMap<>();
        usuariosLogueados.put("Usuarios", this.usuariosTest);

        this.generadorEstadisticas.agregarEstadistica(this.estadisticasUsuarios);
        this.resultadosEstadisticas = this.generadorEstadisticas.generarEstadisticas(usuariosLogueados);

        Assertions.assertEquals(this.resultadosEstadisticas.getResultados().get(this.estadisticasUsuarios), 1);
    }

    @Test
    public void estadisticaEventosTest() {
        this.generadorEstadisticas = new GeneradorEstadisticas();
        this.estadisticasEventos = new EstadisticasEventos();
        Map<String, List<?>> eventosCreados = new HashMap<>();
        eventosCreados.put("Eventos", this.eventosTest);

        this.generadorEstadisticas.agregarEstadistica(this.estadisticasEventos);
        this.resultadosEstadisticas = this.generadorEstadisticas.generarEstadisticas(eventosCreados);

        Assertions.assertEquals(this.resultadosEstadisticas.getResultados().get(this.estadisticasEventos), 1);
    }

    @Test
    public void estadisticaTicketsTest() {

        this.generadorEstadisticas = new GeneradorEstadisticas();
        this.estadisticasTickets = new EstadisticasTickets();

        this.usuarioTest.resevarTicket(this.eventoTest, this.ubicacionTest);
        Map<String, List<?>> ticketsVendidos = new HashMap<>();

        ticketsVendidos.put("Tickets", this.usuarioTest.getTicketsAsociados());

        this.generadorEstadisticas.agregarEstadistica(this.estadisticasTickets);
        this.resultadosEstadisticas = this.generadorEstadisticas.generarEstadisticas(ticketsVendidos);

        Assertions.assertEquals(this.resultadosEstadisticas.getResultados().get(this.estadisticasTickets), 1);
    }

    @Test
    public void estadisticasTest() {
        this.generadorEstadisticas = new GeneradorEstadisticas();
        this.estadisticasUsuarios = new EstadisticasUsuarios();
        this.estadisticasEventos = new EstadisticasEventos();
        this.estadisticasTickets = new EstadisticasTickets();
        this.usuarioTest.resevarTicket(this.eventoTest, this.ubicacionTest);
        Map<String, List<?>> cosasParaEstadisticas = new HashMap<>();
        cosasParaEstadisticas.put("Usuarios", this.usuariosTest);
        cosasParaEstadisticas.put("Eventos", this.eventosTest);
        cosasParaEstadisticas.put("Tickets", this.usuarioTest.getTicketsAsociados());
        this.generadorEstadisticas.agregarEstadistica(this.estadisticasUsuarios);
        this.generadorEstadisticas.agregarEstadistica(this.estadisticasEventos);
        this.generadorEstadisticas.agregarEstadistica(this.estadisticasTickets);
        this.resultadosEstadisticas = this.generadorEstadisticas.generarEstadisticas(cosasParaEstadisticas);

        Assertions.assertEquals(this.resultadosEstadisticas.getResultados().values().stream().mapToInt(Integer::intValue).sum(), 3);
    }
}