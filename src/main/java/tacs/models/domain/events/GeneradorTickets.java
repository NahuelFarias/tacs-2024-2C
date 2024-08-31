package tacs.models.domain.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneradorTickets {

    public List<Ubicacion> ubicaciones;
    public Map<String,Integer> mapaTickets;
    public Evento evento;

    public GeneradorTickets(List<Ubicacion> ubicaciones, Map<String, Integer> mapaTickets) {
        this.ubicaciones = ubicaciones;
        this.mapaTickets = mapaTickets;
    }

    public List<Ticket> generar(Evento eventoAsociado) {
        return this.mapaTickets.entrySet().stream()
                .flatMap(entry -> IntStream.range(0, entry.getValue())
                        .mapToObj(i -> new Ticket(eventoAsociado,buscarUbicacion(entry.getKey()))))
                .collect(Collectors.toList());
    }

    private Ubicacion buscarUbicacion(String nombreUbicacion) {
        return this.ubicaciones.stream().filter(u -> u.getNombre().equals(nombreUbicacion)).
                collect(Collectors.toList()).get(0);
    }
}
