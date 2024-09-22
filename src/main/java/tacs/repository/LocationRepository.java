package tacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tacs.models.domain.events.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

}
