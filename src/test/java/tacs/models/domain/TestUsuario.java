package tacs.models.domain;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tacs.models.domain.users.Usuario;

public class TestUsuario{

    @Test
    public void generarUsuarioTest() {
        String nombreUsuario = "Pepe Rodriguez";

        Usuario usuario = new Usuario(nombreUsuario);
        Assertions.assertEquals(usuario.username, nombreUsuario);
    }
}
