package tacs.models.domain.events;

import tacs.models.domain.users.Usuario;

public class Ticket {
    public double precio;
    public String butaca;
    public Evento eventoAsociado;
    public boolean fueUsado;
    public Usuario duenio;

    public Ticket(double precio, String butaca, Evento eventoAsociado) {
        this.precio = precio;
        this.butaca = butaca;
        this.eventoAsociado = eventoAsociado;
        this.fueUsado = false;
    }

    public void cambiarDuenio(Usuario nuevoDuenio) {
        this.duenio = nuevoDuenio;
    }

    public void consumite() {
        this.fueUsado = false;
    }

    public double getPrecio() {
        return this.precio;
    }

    public boolean fueUsado() {
        return this.fueUsado;
    }
}
