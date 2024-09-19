package tacs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tacs.models.domain.users.NormalUser;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<NormalUser, Integer> {
    List<NormalUser> findAllByLastLoginBetween(LocalDateTime startDate, LocalDateTime endDate);
}