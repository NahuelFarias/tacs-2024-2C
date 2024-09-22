package tacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tacs.models.domain.events.Evento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    List<Evento> findByFechaCreacionBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT e FROM Evento e WHERE LOWER(e.nombre) = LOWER(:nombre)")
    Optional<Evento> findByNombreNormalizado(@Param("nombre") String nombre);
}