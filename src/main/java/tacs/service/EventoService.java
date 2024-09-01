package tacs.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tacs.dto.CrearGenerador;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.GeneradorTickets;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Ubicacion;
import tacs.repository.EventoRepository;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public void createEvent(String nombre, LocalDateTime fecha, CrearGenerador crearGenerador) {
        GeneradorTickets generador = new GeneradorTickets(crearGenerador.getUbicaciones(), crearGenerador.getMapaTickets());
        Evento evento = new Evento(nombre, fecha, generador);
        eventoRepository.save(evento);
    }

    public Evento getEvento(Integer id) {
        return eventoRepository.findById(id).orElse(null);
    }

    public void setState(Integer id, Boolean state) {
        Evento evento = getEvento(id);
        evento.modificarVenta(state); 
    }

    public void createReserves(Integer id, Ubicacion ubicacion) {
        Evento evento = getEvento(id);
        evento.realizarReserva(ubicacion);
    }

    public long getTicketsForSale(Integer id) {
        Evento evento = getEvento(id);
        return evento.getCantidadTicketsDisponibles();
    }

}
