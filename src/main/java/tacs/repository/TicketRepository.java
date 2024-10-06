package tacs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import tacs.models.domain.events.Ticket;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findAllByReservationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Ticket> findAllByUserId(String userId);
}