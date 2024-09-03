package tacs.service;

import org.springframework.stereotype.Service;
import tacs.dto.RespuestaEstadisticas;
import tacs.dto.RespuestaEstadisticasPorEvento;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.Usuario;
import tacs.repository.EventoRepository;
import tacs.repository.TicketRepository;
import tacs.repository.UsuarioRepository;
import tacs.statistics.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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
}
