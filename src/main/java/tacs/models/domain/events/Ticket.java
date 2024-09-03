package tacs.models.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import tacs.models.domain.users.Usuario;

@Entity
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    private boolean vendido;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;
    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento eventoAsociado;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario duenio;

    public Ticket(Evento eventoAsociado, Ubicacion ubicacion) {
        this.eventoAsociado = eventoAsociado;
        this.ubicacion = ubicacion;
        this.vendido = false;
    }

    public void cambiarDuenio(Usuario nuevoDuenio) {
        this.duenio = nuevoDuenio;
    }

    public void seVendio() {
        this.vendido = true;
    }

    public boolean vendido() {
        return this.vendido;
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
