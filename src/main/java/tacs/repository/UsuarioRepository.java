package tacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.Usuario;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByUltimoLoginBetween(LocalDateTime startDate, LocalDateTime endDate);
}