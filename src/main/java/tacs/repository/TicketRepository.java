package tacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tacs.models.domain.events.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
