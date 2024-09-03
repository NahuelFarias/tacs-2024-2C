package tacs.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Ubicacion;
import tacs.models.domain.users.Usuario;
import tacs.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void createUsuario(String name) {
        usuarioRepository.save(new Usuario(name));
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuario(Integer id) {
        Optional<Usuario> opcUsuario = usuarioRepository.findById(id);
        if (opcUsuario.isPresent()) {
            Usuario user = opcUsuario.get();
            return user;
        } 
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public List<Ticket> getReserves(Integer id) {
        Usuario user = this.getUsuario(id);
        return user.getTicketsAsociados();
    }

    @Transactional
    public void resevarTicket(Integer id, Evento evento, Ubicacion ubicacion) {
        Usuario user = this.getUsuario(id);
        user.resevarTicket(evento, ubicacion);
    }

}
