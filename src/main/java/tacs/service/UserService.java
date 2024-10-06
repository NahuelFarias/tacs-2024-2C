package tacs.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import tacs.config.CustomPBKDF2PasswordEncoder;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;
import tacs.repository.TicketRepository;
import tacs.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TicketRepository ticketsRepository;

    public void createUser(String name, String password) {
        String encodedPassword = new CustomPBKDF2PasswordEncoder().encode(password);
        NormalUser newUser = new NormalUser(name);
        newUser.setHashedPassword(encodedPassword);
        userRepository.save(newUser);
    }

    public List<NormalUser> getAllUsers() {
        return userRepository.findAll();
    }

    public NormalUser getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<Ticket> getReservations(String id) {
        return ticketsRepository.findAllByUserId(id);
    }

    //TODO: Meter una cola para mantener consistente las reservas
    public void reserveTicket(String id, Ticket ticket) {
        NormalUser user = this.getUser(id);
        user.bookTicket(ticket);
        ticketsRepository.save(ticket);
        user.addTicket(ticket.getId());
        userRepository.save(user);
    }
}
