package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.GeneradorTickets;
import tacs.models.domain.events.Ubicacion;
import tacs.models.domain.users.Admin;
import tacs.models.domain.users.Usuario;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestAdmin {

    private Admin adminTest;
    private Evento eventoTest;
    private Ubicacion ubicacionTest;

    @BeforeEach
    public void setUp() {
        String nombreUsuario = "Pepa Admin";
        this.adminTest = new Admin(nombreUsuario);

        Ubicacion preferencia = new Ubicacion("Preferencia", 500);
        Ubicacion eastStand = new Ubicacion("East Stand", 200);
        Ubicacion tribunaNorte = new Ubicacion("Tribuna Norte", 400);
        Ubicacion gradaSur = new Ubicacion("Grada Sur", 100);

        List<Ubicacion> ubicaciones = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        Map<String, Integer> mapaTickets = Map.of(
                "Preferencia", 1,
                "East Stand", 11,
                "Tribuna Norte", 50,
                "Grada Sur", 23
        );

        GeneradorTickets generador = new GeneradorTickets(ubicaciones,mapaTickets);

        this.eventoTest = new Evento("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),generador);
        this.ubicacionTest = preferencia;
    }

    @Test
    public void cerrarEventoTest() {
        this.adminTest.cerrarEvento(eventoTest);
        Assertions.assertEquals(eventoTest.ventaAbierta(),false);
    }

}
