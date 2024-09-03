package tacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import tacs.models.domain.events.Evento;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    List<Evento> findByFechaCreacionBetween(LocalDateTime startDate, LocalDateTime endDate);
}