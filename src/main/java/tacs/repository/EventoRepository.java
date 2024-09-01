package tacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tacs.models.domain.events.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

}
