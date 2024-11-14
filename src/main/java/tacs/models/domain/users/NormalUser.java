package tacs.models.domain.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tacs.models.domain.events.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@NoArgsConstructor
public class NormalUser {
    @Getter
    @Id
    private String id;
    @Getter
    @Setter
    @JsonProperty("username")
    public String username;
    @Setter
    @Getter
    public String hashedPassword;
    @Setter
    @Getter
    public String salt;
    @JsonProperty("tickets")
    private List<String> ticketIds; // Lista de IDs de tickets comprados
    @Getter
    @Setter
    public LocalDateTime lastLogin;
    @Setter
    @Getter
    public Rol rol;
    private String email;

    public NormalUser(String username) {
        this.username = username;
        this.ticketIds = new ArrayList<>();
        //Usuario normal por defecto
        this.rol = new Rol("ROLE_USER");
    }

    public List<String> getTicketsOwned() {
        return this.ticketIds;
    }

    public void addTickets(List<String> ticketIds) {
        this.ticketIds.addAll(ticketIds);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
