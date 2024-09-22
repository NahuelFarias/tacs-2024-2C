package tacs.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tacs.dto.CrearGenerador;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.GeneradorTickets;
import tacs.models.domain.events.Ubicacion;
import tacs.repository.EventoRepository;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioService usuarioService;

    public void createEvent(String nombre, LocalDateTime fecha, CrearGenerador crearGenerador) {
        GeneradorTickets generador = new GeneradorTickets(crearGenerador.getUbicaciones(), crearGenerador.getMapaTickets());
        Evento evento = new Evento(nombre, fecha, generador);
        eventoRepository.save(evento);
    }

    public Evento getEvento(Integer id) {
        Optional<Evento> opcEvento = eventoRepository.findById(id);
        if (opcEvento.isPresent()) {
            Evento evento = opcEvento.get();
            return evento;
        } 
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
    }

    public Evento getEventByName(String nombre) {
        Optional<Evento> opcEvento = eventoRepository.findByNombreNormalizado(nombre);
        if (opcEvento.isPresent()) {
            return opcEvento.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
    }

    public List<Evento> getEvents() {
        return eventoRepository.findAll();
    }

    @Transactional
    public void setState(Integer id, Boolean state) {
        Evento evento = this.getEvento(id);
        evento.modificarVenta(state);
    }

    @Transactional
    public void createReserves(Integer id, Integer userId, Ubicacion ubicacion) {
        Evento evento = this.getEvento(id);
        usuarioService.resevarTicket(userId, evento, ubicacion);
    }

    public long getTicketsForSale(Integer id) {
        Evento evento = this.getEvento(id);
        return evento.getCantidadTicketsDisponibles();
    }

}
