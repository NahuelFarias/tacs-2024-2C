package tacs.models.domain.users;

import tacs.models.domain.events.Evento;
import java.util.List;

public class Admin {
    public String username;
    public List<Evento> eventosCreados;

    public Admin(String username) {
        this.username = username;
    }

    public void cerrarEvento(Evento evento) {
        evento.cerrarVenta();
    }

}
