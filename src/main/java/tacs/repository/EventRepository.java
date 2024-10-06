package tacs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tacs.models.domain.events.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findAllByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Optional<Event> findByNormalizedName(String name);
}