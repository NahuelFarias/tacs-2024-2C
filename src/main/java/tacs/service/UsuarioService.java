package tacs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tacs.models.domain.events.Ticket;
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
        return usuarioRepository.findById(id).orElse(null);
    }

    public List<Ticket> getReserves(Integer id) {
        Usuario user = getUsuario(id);
        return user.getTicketsAsociados();
    }

}
