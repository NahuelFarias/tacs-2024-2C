package tacs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import tacs.models.domain.users.NormalUser;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<NormalUser, String> {
    List<NormalUser> findAllByLastLoginBetween(LocalDateTime startDate, LocalDateTime endDate);
    NormalUser findByUsername(String username);
}