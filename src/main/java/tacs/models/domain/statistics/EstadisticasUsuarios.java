package tacs.models.domain.statistics;

import tacs.models.domain.users.Usuario;

import java.util.List;

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
