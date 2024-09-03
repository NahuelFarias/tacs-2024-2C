package tacs.statistics;

import tacs.models.domain.users.Usuario;

import java.util.List;

public class EstadisticasUsuarios implements Estadisticas<Usuario>{

    @Override
    public Integer generarEstadistica(List<Usuario> usuarios) {
        return (int) usuarios.stream().filter(u -> u.getUltimoLogin() != null).count();
    }

    @Override
    public String name() {
        return "Usuarios";
    }
}
