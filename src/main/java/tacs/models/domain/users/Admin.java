package tacs.models.domain.users;

import tacs.models.domain.events.Event;
import java.util.List;

public class Admin {
    public String username;
    public List<Event> eventsCreated;

    public Admin(String username) {
        this.username = username;
    }

    public void disableEvent(Event event) {
        event.closeSale();
    }

}
