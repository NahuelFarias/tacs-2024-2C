package tacs.telegram.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.dto.CreateEvent;
import tacs.dto.LocationDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AdminCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(AdminCommandHandler.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Autowired
    private RestTemplate restTemplate;

    public SendMessage handleCreateEventCommand(long chatId, CreateEventData data, String messageText) {
        switch (data.state) {
            case AWAITING_NAME:
                data.name = messageText;
                data.state = CreateEventState.AWAITING_DATE;
                return sendMessage(chatId, "Ingresa la fecha y hora del evento (formato: dd-MM-yyyy HH:mm):\n(Use /cancel para cancelar el proceso)");
                
            case AWAITING_DATE:
                try {
                    LocalDateTime.parse(messageText, formatter);
                    data.date = messageText;
                    data.state = CreateEventState.AWAITING_IMAGE_URL;
                    return sendMessage(chatId, "Ingresa la URL de la imagen del evento:\n(Use /cancel para cancelar el proceso)");
                } catch (DateTimeParseException e) {
                    return sendMessage(chatId, "Formato de fecha inválido. Usa el formato dd-MM-yyyy HH:mm\nPor ejemplo: 25-12-2024 20:30\n(Use /cancel para cancelar el proceso)");
                }
                
            case AWAITING_IMAGE_URL:
                data.imageUrl = messageText;
                data.state = CreateEventState.AWAITING_LOCATION_NAME;
                return sendMessage(chatId, "Ingresa el nombre de la ubicación:\n(Use /cancel para cancelar el proceso)");
                
            case AWAITING_LOCATION_NAME:
                data.tempLocationName = messageText;
                data.state = CreateEventState.AWAITING_LOCATION_PRICE;
                return sendMessage(chatId, "Ingresa el precio para esta ubicación:\n(Use /cancel para cancelar el proceso)");
                
            case AWAITING_LOCATION_PRICE:
                try {
                    data.tempLocationPrice = Double.parseDouble(messageText);
                    data.state = CreateEventState.AWAITING_LOCATION_QUANTITY;
                    return sendMessage(chatId, "Ingresa la cantidad de tickets disponibles para esta ubicación:\n(Use /cancel para cancelar el proceso)");
                } catch (NumberFormatException e) {
                    return sendMessage(chatId, "Por favor, ingresa un número válido para el precio:\n(Use /cancel para cancelar el proceso)");
                }
                
            case AWAITING_LOCATION_QUANTITY:
                try {
                    int quantity = Integer.parseInt(messageText);
                    Location location = new Location(data.tempLocationName, data.tempLocationPrice, quantity);
                    data.locations.add(location);
                    
                    // Limpiar variables temporales
                    data.tempLocationName = null;
                    data.tempLocationPrice = null;
                    
                    data.state = CreateEventState.AWAITING_MORE_LOCATIONS;
                    return sendMessage(chatId, "¿Deseas agregar otra ubicación? (si/no)\n(Use /cancel para cancelar el proceso)");
                } catch (NumberFormatException e) {
                    return sendMessage(chatId, "Por favor, ingresa un número válido para la cantidad:\n(Use /cancel para cancelar el proceso)");
                }
            
            case AWAITING_MORE_LOCATIONS:
                if (messageText.toLowerCase().equals("si")) {
                    // Verificar límite de ubicaciones (por ejemplo, máximo 5)
                    if (data.locations.size() >= 15) {
                        return sendMessage(chatId, "Has alcanzado el límite máximo de ubicaciones. Procediendo a crear el evento...");
                    }
                    data.state = CreateEventState.AWAITING_LOCATION_NAME;
                    return sendMessage(chatId, "Ingresa el nombre de la nueva ubicación:\n(Use /cancel para cancelar el proceso)");
                } else if (messageText.toLowerCase().equals("no")) {
                    return createEvent(chatId, data);
                } else {
                    return sendMessage(chatId, "Por favor, responde 'si' o 'no':\n(Use /cancel para cancelar el proceso)");
                }
                
            default:
                return sendMessage(chatId, "Error en el proceso de creación. Por favor, intenta nuevamente con /createEvent");
        }
    }

    public SendMessage handleCloseEventCommand(long chatId, String eventName) {
        try {
            Event event = restTemplate.getForObject(
                "http://localhost:8080/events/search?name=" + eventName,
                Event.class
            );
            
            if (event == null) {
                return sendMessage(chatId, "No se encontró ningún evento con ese nombre.");
            }

            restTemplate.exchange(
                "http://localhost:8080/events/" + event.getId() + "/close",
                HttpMethod.PUT,
                null,
                Void.class
            );

            return sendMessage(chatId, "El evento '" + eventName + "' ha sido cerrado exitosamente.");
        } catch (Exception e) {
            logger.error("Error al cerrar evento: ", e);
            return sendMessage(chatId, "Ocurrió un error al intentar cerrar el evento.");
        }
    }

    private SendMessage createEvent(long chatId, CreateEventData data) {
        try {
            CreateEvent event = new CreateEvent();
            event.setName(data.name);
            event.setDate(LocalDateTime.parse(data.date, formatter));
            event.setImageUrl(data.imageUrl);
            List<LocationDTO> locationDTOs = data.locations.stream()
                .map(loc -> new LocationDTO(loc.getName(), loc.getPrice(), loc.getQuantityTickets()))
                .collect(Collectors.toList());
            event.setLocations(locationDTOs);
            
            restTemplate.postForEntity("http://localhost:8080/events", event, Void.class);
            
            // En lugar de setear el estado a null, debemos indicar al Bot que remueva el objeto del mapa
            return sendMessage(chatId, "¡Evento creado exitosamente!");
        } catch (Exception e) {
            logger.error("Error al crear evento: ", e);
            return sendMessage(chatId, "Error al crear el evento. Por favor, intenta nuevamente.");
        }
    }

    private SendMessage sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("Markdown");
        return message;
    }

    public static class CreateEventData {
        public String name;
        public String date;
        public String imageUrl;
        public List<Location> locations = new ArrayList<>();
        public String tempLocationName;
        public Double tempLocationPrice;
        public CreateEventState state;

        public CreateEventData(CreateEventState state) {
            this.state = state;
        }
    }

    public enum CreateEventState {
        AWAITING_NAME,
        AWAITING_DATE,
        AWAITING_IMAGE_URL,
        AWAITING_LOCATION_NAME,
        AWAITING_LOCATION_PRICE,
        AWAITING_LOCATION_QUANTITY,
        AWAITING_MORE_LOCATIONS
    }
} 