package tacs.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    @Autowired
    private MongoTemplate mongoTemplate;

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

    public void makeReservation(String id, List<Ticket> tickets) {
        NormalUser user = this.getUser(id);

        tickets.forEach(ticket -> ticket.changeOwner(id));
        tickets.forEach(ticket -> ticket.setReservationDate(LocalDateTime.now()));

        ticketsRepository.saveAll(tickets);

        List<String> ticketIds = tickets.stream().map(Ticket::getId).toList();

        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        Update update = new Update().addToSet("ticketIds").each(ticketIds);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

        NormalUser updatedUser = mongoTemplate.findAndModify(query, update, options, NormalUser.class);
    }
}
