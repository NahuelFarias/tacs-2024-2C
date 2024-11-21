package tacs.telegram.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.events.Ticket;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

@Component
public class UserCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserCommandHandler.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy 'a las' HH:mm");

    @Autowired
    private RestTemplate restTemplate;

    public SendMessage handleEventsCommand(long chatId) {
        String eventsUrl = "http://localhost:8080/events";
        Event[] events = restTemplate.getForObject(eventsUrl, Event[].class);

        StringBuilder responseBuilder = new StringBuilder("Eventos disponibles 🤘\n\n");
        if (events != null && events.length > 0) {
            for (Event event : events) {
                responseBuilder.append(formatEvent(event)).append("\n");
            }
        } else {
            responseBuilder.append("No se encontraron eventos 😥");
        }

        return sendMessage(chatId, responseBuilder.toString());
    }

    public SendMessage handleSearchCommand(long chatId, String searchQuery) {
        String eventsUrl = "http://localhost:8080/events";
        Event[] events = restTemplate.getForObject(eventsUrl, Event[].class);

        StringBuilder responseBuilder = new StringBuilder("Se encontraron los eventos: \n\n");
        if (events != null && events.length > 0) {
            for (Event event : events) {
                if (event.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    responseBuilder.append(formatEvent(event)).append("\n");
                }
            }
        } else {
            responseBuilder.append("No se encontraron eventos 😥");
        }

        return sendMessage(chatId, responseBuilder.toString());
    }

    public SendMessage handleEventDetailCommand(long chatId, String eventName) {
        String eventDetailUrl = "http://localhost:8080/events/search?name=" + eventName;

        try {
            Event event = restTemplate.getForObject(eventDetailUrl, Event.class);
            if (event != null) {
                StringBuilder detailBuilder = new StringBuilder();
                detailBuilder.append("Detalles del evento:\n\n");
                detailBuilder.append("Nombre: ").append(event.getName()).append("\n");
                detailBuilder.append("Fecha: ").append(event.getDate().format(formatter)).append("\n");
                detailBuilder.append("Poster: [Imagen del evento]").append("(").append(event.getImageUrl()).append(")\n\n");
                
                detailBuilder.append("Asientos disponibles:\n");
                if (!event.isOpenSale()) {
                    detailBuilder.append("La venta de entradas aún no está abierta.\n");
                }
                
                if (event.getLocations() != null && !event.getLocations().isEmpty()) {
                    for(Location zone : event.getLocations()) {
                        detailBuilder.append("- ").append(zone.getName()).append(": \n\t")
                                     .append("Precio: $").append(zone.getPrice()).append("\n\t")
                                     .append("Tickets disponibles: ").append(zone.getQuantityTickets())
                                     .append("\n");
                    }
                } else {
                    detailBuilder.append("No hay información de ubicación disponible.\n");
                }

                return sendMessage(chatId, detailBuilder.toString());
            } else {
                return sendMessage(chatId, "No encontramos ningun evento que coincida con la busqueda de '" + eventName + "' 😥");
            }
        } catch (HttpClientErrorException.NotFound e) {
            return sendMessage(chatId, "No encontramos ningun evento que coincida con la busqueda de '" + eventName + "' 😥");
        } catch (Exception e) {
            return sendMessage(chatId, "Un error inesperado ocurrio en la busqueda, reintente en unos momentos");
        }
    }

    public SendMessage handleMyTicketsCommand(long chatId, String userId) {
        if (userId == null) {
            return sendMessage(chatId, "Debes iniciar sesión para ver tus tickets. Usa /login");
        }

        try {
            ResponseEntity<List<Ticket>> response = restTemplate.exchange(
                "http://localhost:8080/users/" + userId + "/reserves",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Ticket>>() {}
            );

            List<Ticket> tickets = response.getBody();
            
            if (tickets == null || tickets.isEmpty()) {
                return sendMessage(chatId, "No tienes tickets reservados 😢");
            }

            Map<String, Map<String, Integer>> groupedTickets = new HashMap<>();

            for (Ticket ticket : tickets) {
                String eventId = ticket.getEventId();
                Event event = restTemplate.getForObject(
                    "http://localhost:8080/events/" + eventId,
                    Event.class
                );
                
                if (event == null) continue;

                groupedTickets.putIfAbsent(event.getId(), new HashMap<>());
                Map<String, Integer> locationCounts = groupedTickets.get(event.getId());

                Location location = event.getLocations().stream()
                    .filter(loc -> loc.getId().equals(ticket.getLocationId()))
                    .findFirst()
                    .orElse(null);

                if (location != null) {
                    String locationName = location.getName();
                    locationCounts.put(locationName, locationCounts.getOrDefault(locationName, 0) + 1);
                }
            }

            StringBuilder message = new StringBuilder("Tus tickets reservados 🎫\n\n");
            
            for (Map.Entry<String, Map<String, Integer>> eventEntry : groupedTickets.entrySet()) {
                Event event = restTemplate.getForObject(
                    "http://localhost:8080/events/" + eventEntry.getKey(),
                    Event.class
                );
                
                if (event != null) {
                    message.append("Evento: ").append(event.getName()).append("\n")
                           .append("Fecha: ").append(event.getDate().format(formatter)).append("\n")
                           .append("Ubicaciones:\n");

                    for (Map.Entry<String, Integer> locationEntry : eventEntry.getValue().entrySet()) {
                        message.append("  - ").append(locationEntry.getKey())
                               .append(": ").append(locationEntry.getValue())
                               .append(" ticket(s)\n");
                    }
                    message.append("\n");
                }
            }

            return sendMessage(chatId, message.toString());
        } catch (Exception e) {
            logger.error("Error al obtener tickets: ", e);
            return sendMessage(chatId, "Ocurrió un error al obtener tus tickets. Por favor, intenta nuevamente.");
        }
    }

    private String formatEvent(Event event) {
        return String.format("Nombre: %s\nFecha: %s\nTickets disponibles: %s\n",
                event.getName(),
                event.getDate().format(formatter),
                event.getAvailableTickets());
    }

    private SendMessage sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("Markdown");
        return message;
    }
} 