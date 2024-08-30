package tacs.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.GeneradorTickets;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Ubicacion;
import tacs.models.domain.users.Usuario;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class TestTicket {

    private Usuario usuarioTest;
    private Evento eventoTest;
    private Ubicacion ubicacionTest;

    @BeforeEach
    public void setUp() {
        String nombreUsuario = "Pepe Rodriguez";
        this.usuarioTest = new Usuario(nombreUsuario);

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
    public void reservarTicketTest() {
        this.usuarioTest.resevarTicket(this.eventoTest,this.ubicacionTest);
        Assertions.assertEquals(usuarioTest.getTicketsAsociados().size(),1);
    }

    @Test
    public void consultarReservasTest() {
        this.usuarioTest.resevarTicket(this.eventoTest,this.ubicacionTest);
        this.usuarioTest.resevarTicket(this.eventoTest,this.ubicacionTest);
        List<Ticket> reservas = this.usuarioTest.getTicketsAsociados();
        Assertions.assertEquals(reservas.size(),2);
    }
}
