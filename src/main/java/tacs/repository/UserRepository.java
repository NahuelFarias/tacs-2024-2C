package tacs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import tacs.models.domain.users.NormalUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<NormalUser, String> {
    List<NormalUser> findAllByLastLoginBetween(LocalDateTime startDate, LocalDateTime endDate);
    Optional<NormalUser> findByUsername(String username);
}