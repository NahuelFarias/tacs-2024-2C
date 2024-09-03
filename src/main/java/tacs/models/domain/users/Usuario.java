package tacs.models.domain.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import tacs.models.domain.events.Evento;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.events.Ubicacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    @JsonProperty("name")
    public String username;
    @JsonProperty("tickets")
    @OneToMany(mappedBy = "duenio", cascade = CascadeType.ALL)
    public List<Ticket> ticketsAsociados;
    @Column
    public LocalDateTime ultimoLogin;

    public Usuario(String username) {
        this.username = username;
        this.ticketsAsociados = new ArrayList<>();
    }

    public void resevarTicket(Evento evento, Ubicacion ubicacion) {
        Ticket ticket = evento.realizarReserva(ubicacion);
        this.aniadirTicket(ticket);
        ticket.cambiarDuenio(this);
        ticket.setFechaReserva(LocalDateTime.now());
    }

    public void aniadirTicket(Ticket ticketNuevo) {
        this.ticketsAsociados.add(ticketNuevo);
    }

    public List<Ticket> getTicketsAsociados() {
        return this.ticketsAsociados;
    }

    public Optional<Evento> buscarEvento(String nombreEvento) {
        List<Evento> eventosAsociados = this.ticketsAsociados.stream().map(t -> t.getEventoAsociado())
                .collect(Collectors.toList());
        return eventosAsociados.stream().filter(e -> e.getNombre() == nombreEvento)
                .findFirst();
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }
}
