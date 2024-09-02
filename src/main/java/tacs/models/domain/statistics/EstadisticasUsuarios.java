package tacs.models.domain.statistics;

import tacs.models.domain.users.Usuario;

import java.util.List;

public class EstadisticasUsuarios implements Estadisticas<Usuario>{

    private static EstadisticasUsuarios instance;

    public static EstadisticasUsuarios getInstance() {
        if (instance == null) {
            instance = new EstadisticasUsuarios();
        }
        return instance;
    }
    @Override
    public Integer generarEstadistica(List<Usuario> usuarios) {
        return usuarios.size();
    }

    @Override
    public String name() {
        return "Usuarios";
    }
}
