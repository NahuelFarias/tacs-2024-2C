package tacs.models.domain.events;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tickets")
@NoArgsConstructor
public class Ticket {

    @Id
    private String id;
    @Field("eventId")
    private String eventId;
    @Field("locationId")
    private String locationId;
    @Field("userId")
    private String userId;
    public LocalDateTime reservationDate;

    public Ticket(String eventId, String locationId) {
        this.eventId = eventId;
        this.locationId = locationId;
    }

    public void changeOwner(String newOwner) {
        this.userId = newOwner;
    }

    public String getEventId() {
        return eventId;
    }

    public String getLocationId() {
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

    public String getUserId() {
        return userId;
    }

}
