package tacs.models.domain.events;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import tacs.models.domain.users.Usuario;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;
    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento eventoAsociado;
    @Column
    private boolean fueUsado;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario duenio;
    @Column
    public LocalDateTime fechaReserva;

    public Ticket(Evento eventoAsociado, Ubicacion ubicacion) {
        this.eventoAsociado = eventoAsociado;
        this.ubicacion = ubicacion;
        this.fechaReserva = LocalDateTime.now();
        this.fueUsado = false;
    }

    public void cambiarDuenio(Usuario nuevoDuenio) {
        this.duenio = nuevoDuenio;
    }

    public void consumite() {
        this.fueUsado = true;
    }

    public boolean fueUsado() {
        return this.fueUsado;
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

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
}
