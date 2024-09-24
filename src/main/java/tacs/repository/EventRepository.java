package tacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tacs.models.domain.events.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM Event e WHERE LOWER(e.name) = LOWER(:name)")
    Optional<Event> findByNormalizedName(@Param("name") String name);
}