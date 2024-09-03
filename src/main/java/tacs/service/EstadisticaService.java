package tacs.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tacs.dto.RespuestaEstadisticas;
import tacs.dto.RespuestaEstadisticasPorEvento;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.GeneradorTickets;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Ubicacion;
import tacs.models.domain.users.Usuario;
import tacs.repository.EventoRepository;
import tacs.repository.TicketRepository;
import tacs.repository.UsuarioRepository;
import tacs.statistics.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticaService {

    private final EventoRepository repositorioEventos;
    private final TicketRepository repositorioTickets;
    private final UsuarioRepository repositorioUsuarios;

    public EstadisticaService(EventoRepository repositorioEventos, TicketRepository repositorioTickets,
                              UsuarioRepository repositorioUsuarios) {
        this.repositorioEventos = repositorioEventos;
        this.repositorioTickets = repositorioTickets;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    public List<RespuestaEstadisticas> obtenerEstadisticas(){
        this.test();
        List<RespuestaEstadisticas> resultados = new ArrayList<>();

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime primerDiaDelAnio = LocalDateTime.of(ahora.getYear(), Month.JANUARY, 1, 0, 0);
        resultados.add(this.consultarEstadisticaPor(primerDiaDelAnio, ahora,"Anual"));

        LocalDateTime comienzoSemana = ahora.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).
                toLocalDate().atStartOfDay();
        resultados.add(this.consultarEstadisticaPor(comienzoSemana, ahora,"Semanal"));

        LocalDateTime comienzoDia = ahora.toLocalDate().atStartOfDay();
        resultados.add(this.consultarEstadisticaPor(comienzoDia, ahora,"Diario"));
        return resultados;
    }

    private RespuestaEstadisticas consultarEstadisticaPor(LocalDateTime fechaInicio, LocalDateTime fechaFin,
                                                          String rangoTemporal){
        GeneradorEstadisticas generadorEstadisticas = new GeneradorEstadisticas();

        List<Evento> eventosConsultados = this.repositorioEventos.findByFechaCreacionBetween(fechaInicio,fechaFin);
        List<Ticket> ticketsConsultados = this.repositorioTickets.findByFechaReservaBetween(fechaInicio, fechaFin);
        List<Usuario> usuariosConsultados = this.repositorioUsuarios.findByUltimoLoginBetween(fechaInicio,fechaFin);

        EstadisticasEventos estadisticaEventos = new EstadisticasEventos();
        EstadisticasTickets estadisticasTickets = new EstadisticasTickets();
        EstadisticasUsuarios estadisticasUsuarios = new EstadisticasUsuarios();

        generadorEstadisticas.agregarEstadistica(estadisticaEventos);
        generadorEstadisticas.agregarEstadistica(estadisticasTickets);
        generadorEstadisticas.agregarEstadistica(estadisticasUsuarios);

        Map<String, List<?>> datosAnuales = Map.of(
                "Tickets",ticketsConsultados,
                "Eventos",eventosConsultados,
                "Usuarios",usuariosConsultados
        );

        ResultadosEstadisticas estadistica =  generadorEstadisticas.generarEstadisticas(datosAnuales);
        RespuestaEstadisticas respuesta = new RespuestaEstadisticas();
        Map<Estadisticas<?>, Integer> resultadosEstadisticas = estadistica.getResultados();

        respuesta.setRangoTemporal(rangoTemporal);
        respuesta.setCantidadEventos(resultadosEstadisticas.get(estadisticaEventos));
        respuesta.setCantidadTickets(resultadosEstadisticas.get(estadisticasTickets));
        respuesta.setCantidadLogins(resultadosEstadisticas.get(estadisticasUsuarios));
        return respuesta;
    }


    public Integer estadisticaTicketPorId(LocalDateTime fechaInicio, LocalDateTime fechaFin,
                                  String rangoTemporal, int unId) {
        GeneradorEstadisticas generadorEstadisticas = new GeneradorEstadisticas();
        List<Ticket> ticketsConsultados = this.repositorioTickets.findByFechaReservaBetween(fechaInicio, fechaFin);

        // Esto, por ahora propongo simularlo asi, dado que deberia generarse una consulta (dialecto SQL de hibernate)
        // en el repo, y no vamos a utilizar una BBDD de tipo relacional
        ticketsConsultados = ticketsConsultados.stream().filter(t -> t.getEventoAsociado().getId().equals(unId)).collect(Collectors.toList());
        EstadisticasTickets estadisticasTickets = new EstadisticasTickets();
        generadorEstadisticas.agregarEstadistica(estadisticasTickets);

        Map<String, List<?>> datosAnuales = Map.of(
                "Tickets",ticketsConsultados
        );

        ResultadosEstadisticas estadistica =  generadorEstadisticas.generarEstadisticas(datosAnuales);
        Map<Estadisticas<?>, Integer> resultadosEstadisticas = estadistica.getResultados();
        return resultadosEstadisticas.get(estadisticasTickets);
    }

    public RespuestaEstadisticasPorEvento obtenerEstadisticaPorID(int unId) {
        RespuestaEstadisticasPorEvento resultados = new RespuestaEstadisticasPorEvento();
        LocalDateTime ahora = LocalDateTime.now();

        LocalDateTime primerDiaDelAnio = LocalDateTime.of(ahora.getYear(), Month.JANUARY, 1, 0, 0);
        resultados.setCantidadTicketsAnio(this.estadisticaTicketPorId(primerDiaDelAnio, ahora,"Anual",unId));

        LocalDateTime comienzoSemana = ahora.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).
                toLocalDate().atStartOfDay();
        resultados.setCantidadTicketsSemana(this.estadisticaTicketPorId(comienzoSemana, ahora,"Semanal",unId));

        LocalDateTime comienzoDia = ahora.toLocalDate().atStartOfDay();
        resultados.setCantidadTicketsDia(this.estadisticaTicketPorId(comienzoDia, ahora,"Diario",unId));
        return resultados;
    }

    @Transactional
    protected void test() {

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

        GeneradorTickets generador = new GeneradorTickets(ubicaciones,mapaTickets);

        Evento eventoTest = new Evento("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),generador);
        this.repositorioEventos.saveAndFlush(eventoTest);

        Evento eventoTest2 = new Evento("River", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),generador);
        eventoTest2.fechaCreacion = LocalDate.of(2024, Month.FEBRUARY, 9).atStartOfDay();
        this.repositorioEventos.saveAndFlush(eventoTest2);

        Evento eventoTest3 = new Evento("Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),generador);
        eventoTest3.fechaCreacion = LocalDate.of(2024, Month.SEPTEMBER, 2).atTime(0,1);
        this.repositorioEventos.saveAndFlush(eventoTest3);

        Evento eventoTest4 = new Evento("Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),generador);
        eventoTest4.fechaCreacion = LocalDate.of(2024, Month.AUGUST, 24).atTime(11,0);
        this.repositorioEventos.saveAndFlush(eventoTest4);


        String nombreUsuario = "Pepe Rodriguez";
        Usuario usuarioTest= new Usuario(nombreUsuario);
        usuarioTest.setUltimoLogin(LocalDateTime.now());    // Simulamos login
        this.repositorioUsuarios.saveAndFlush(usuarioTest);

        usuarioTest.resevarTicket(eventoTest,preferencia);
        usuarioTest.resevarTicket(eventoTest,eastStand);
        usuarioTest.resevarTicket(eventoTest,eastStand);
        usuarioTest.resevarTicket(eventoTest2,eastStand);

        this.repositorioTickets.saveAllAndFlush(usuarioTest.getTicketsAsociados());
    }
}
