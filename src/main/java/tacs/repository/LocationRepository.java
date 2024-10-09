package tacs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import tacs.models.domain.events.Location;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

}
