package tacs.models.domain.events;

import tacs.models.domain.users.Usuario;

public class Ticket {

    private Ubicacion ubicacion;
    private Evento eventoAsociado;
    private boolean fueUsado;
    private Usuario duenio;

    public Ticket(Evento eventoAsociado, Ubicacion ubicacion) {
        this.eventoAsociado = eventoAsociado;
        this.ubicacion = ubicacion;
        this.fueUsado = false;
    }

    public void cambiarDuenio(Usuario nuevoDuenio) {
        this.duenio = nuevoDuenio;
    }

    public void consumite() {
        this.fueUsado = false;
    }

    public boolean getFueUsado() {
        return this.fueUsado;
    }
    public void ticketTomado() {
        this.fueUsado = true;
    }

    public Evento getEventoAsociado() {
        return eventoAsociado;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public double buscarPrecio() {
        return this.ubicacion.getPrecio();
    }
}
