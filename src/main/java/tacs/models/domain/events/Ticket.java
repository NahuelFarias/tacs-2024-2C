package tacs.models.domain.events;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
@NoArgsConstructor
public class Ticket {

    @Id
    private String id;
    private String eventId; // ID del evento relacionado
    private String locationId; // ID de la ubicación relacionada
    private String userId; // ID del usuario que compró el ticket
    public LocalDateTime reservationDate;

    public Ticket(String eventId, String locationId) {
        this.eventId = eventId;
        this.locationId = locationId;
    }

    public void changeOwner(String newOwner) {
        this.userId = newOwner;
    }

    public String getEvent() {
        return eventId;
    }

    public String getLocation() {
        return locationId;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

}
