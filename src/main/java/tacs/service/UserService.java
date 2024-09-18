package tacs.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.User;
import tacs.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createUser(String name) {
        userRepository.save(new User(name));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUsers(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<Ticket> getReservations(Integer id) {
        User user = this.getUsers(id);
        return user.getOwnedTickets();
    }

    @Transactional
    public void reserveTicket(Integer id, Event event, Location location) {
        User user = this.getUsers(id);
        user.reserveTicket(event, location);
    }

}
