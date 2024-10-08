package tacs.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.NormalUser;
import tacs.repository.TicketRepository;
import tacs.repository.UserRepository;
import tacs.security.CustomPBKDF2PasswordEncoder;

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

    //TODO: Meter una cola para mantener consistente las reservas
    public void reserveTickets(String id, List<Ticket> tickets) {
        tickets.forEach(ticket -> this.reserveTicket(id, ticket));
    }
}
