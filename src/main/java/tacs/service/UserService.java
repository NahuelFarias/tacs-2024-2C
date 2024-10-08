package tacs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tacs.config.CustomPBKDF2PasswordEncoder;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.NormalUser;
import tacs.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createUser(String name, String password) {
        String encodedPassword = new CustomPBKDF2PasswordEncoder().encode(password);
        NormalUser newUser = new NormalUser(name);
        newUser.setHashedPassword(encodedPassword);
        userRepository.save(newUser);
    }

    public List<NormalUser> getAllUsers() {
        return userRepository.findAll();
    }

    public NormalUser getUsers(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<Ticket> getReservations(Integer id) {
        NormalUser user = this.getUsers(id);
        return user.getTicketsOwned();
    }


    public void reserveTicket(NormalUser user, Ticket ticket){
        user.bookTicket(ticket);
        userRepository.save(user);
    }

    //TODO: Meter una cola para mantener consistente las reservas
    public void reserveTickets(Integer id, List<Ticket> tickets) {
        NormalUser user = this.getUsers(id);
        tickets.forEach(ticket -> this.reserveTicket(user, ticket));
    }
}
