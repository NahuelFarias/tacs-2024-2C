package tacs.models.domain.statistics;

import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.Usuario;

import java.util.List;
import java.util.stream.Stream;

public class EstadisticasUsuarios implements Estadisticas<Usuario>{

    @Override
    public Integer generarEstadistica(List<Usuario> usuarios) {
        return usuarios.size();
    }

    @Override
    public String name() {
        return "Usuarios";
    }
}
