package tacs.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tacs.config.CustomPBKDF2PasswordEncoder;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;
import tacs.repository.UserRepository;

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

    @Transactional
    public void reserveTicket(Integer id, Event event, Location location) {
        NormalUser user = this.getUsers(id);
        user.bookTicket(event, location);
    }
}
