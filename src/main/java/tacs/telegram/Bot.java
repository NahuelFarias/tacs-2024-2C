package tacs.telegram;

import java.util.Random;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import tacs.dto.CreateUser;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;


import tacs.dto.CreateReservation;
import tacs.telegram.handlers.UserCommandHandler;
import tacs.telegram.handlers.AdminCommandHandler;
import org.springframework.http.HttpMethod;
import java.util.stream.Collectors;

@Component
public class Bot extends TelegramWebhookBot {
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.path}")
    private String botPath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserCommandHandler userCommandHandler;

    @Autowired
    private AdminCommandHandler adminCommandHandler;

    private Map<Long, RegistrationData> registrationData = new HashMap<>();

    private Map<Long, String> loggedInUsers = new HashMap<>();

    private Map<Long, LoginData> loginData = new HashMap<>();

    private Map<Long, ReservationData> reservationData = new HashMap<>();

    private Map<Long, Boolean> adminUsers = new HashMap<>();

    private Map<Long, AdminCommandHandler.CreateEventData> createEventData = new HashMap<>();

    private static class RegistrationData {
        String username;
        String password;
        String passwordConfirmation;
        String email;
        String verificationToken;
        RegistrationState state;

        RegistrationData(RegistrationState state) {
            this.state = state;
        }
    }

    private enum RegistrationState {
        AWAITING_USERNAME,
        AWAITING_PASSWORD,
        AWAITING_PASSWORD_CONFIRMATION,
        AWAITING_EMAIL,
        AWAITING_VERIFICATION_TOKEN
    }

    private enum LoginState {
        AWAITING_USERNAME,
        AWAITING_VERIFICATION_CODE
    }

    private static class LoginData {
        String username;
        String verificationToken;
        LoginState state;

        LoginData(LoginState state) {
            this.state = state;
        }
    }

    private enum ReservationState {
        AWAITING_EVENT_NAME,
        AWAITING_LOCATION,
        AWAITING_QUANTITY
    }

    private static class ReservationData {
        String eventId;
        String eventName;
        String locationName;
        int quantity;
        ReservationState state;

        ReservationData(ReservationState state) {
            this.state = state;
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/cancel")) {
                return handleCancelCommand(chatId);
            }

            if (registrationData.containsKey(chatId)) {
                return handleRegistrationState(chatId, messageText);
            }

            if (loginData.containsKey(chatId)) {
                return handleLoginState(chatId, messageText);
            }

            if (reservationData.containsKey(chatId)) {
                return handleReservationState(chatId, messageText);
            }

            if (createEventData.containsKey(chatId)) {
                SendMessage response = adminCommandHandler.handleCreateEventCommand(chatId, createEventData.get(chatId), messageText);
                if (response.getText().equals("¡Evento creado exitosamente!")) {
                    createEventData.remove(chatId);  // Removemos el objeto cuando el evento se crea exitosamente
                }
                return response;
            }
    

            String[] commandParts = messageText.split(" ", 2);
            String command = commandParts[0];

            switch (command) {
                case "/events":
                    return userCommandHandler.handleEventsCommand(chatId);
                case "/search":
                    if (commandParts.length > 1) {
                        return userCommandHandler.handleSearchCommand(chatId, commandParts[1].trim());
                    } else {
                        return sendMessage(chatId, "Please provide a search query. Usage: /search <query>");
                    }
                case "/eventDetail":
                    if (commandParts.length > 1) {
                        return userCommandHandler.handleEventDetailCommand(chatId, commandParts[1].trim());
                    } else {
                        return sendMessage(chatId, "Please provide an event name. Usage: /eventDetail <event name>");
                    }
                case "/register":
                    //TODO: Deja crear mas de un user con el mismo nombre de usuario
                    return handleRegisterCommand(chatId);
                case "/help":
                    return handleHelpCommand(chatId);
                case "/logout":
                    return handleLogoutCommand(chatId);
                case "/login":
                    return handleLoginCommand(chatId);
                case "/reserve":
                    return handleReserveCommand(chatId);
                case "/myTickets":
                    return userCommandHandler.handleMyTicketsCommand(chatId, loggedInUsers.get(chatId));
                case "/admin":
                    return handleAdminCommand(chatId);
                case "/createEvent":
                    if (!adminUsers.getOrDefault(chatId, false)) {
                        return sendMessage(chatId, "Este comando está disponible solo para administradores.");
                    }
                    createEventData.put(chatId, new AdminCommandHandler.CreateEventData(AdminCommandHandler.CreateEventState.AWAITING_NAME));
                    return sendMessage(chatId, "Por favor, ingresa el nombre del evento:\n(Use /cancel para cancelar el proceso)");
                case "/closeEvent":
                    if (!adminUsers.getOrDefault(chatId, false)) {
                        return sendMessage(chatId, "Este comando está disponible solo para administradores.");
                    }
                    if (commandParts.length > 1) {
                        return adminCommandHandler.handleCloseEventCommand(chatId, commandParts[1].trim());
                    } else {
                        return sendMessage(chatId, "Por favor proporciona el nombre del evento. Uso: /closeEvent <nombre del evento>");
                    }
                default:
                    return sendMessage(chatId, "Unknown command. Use /help to see available commands.");
            }
        }
        return null;
    }

    private SendMessage handleRegisterCommand(long chatId) {
        registrationData.put(chatId, new RegistrationData(RegistrationState.AWAITING_USERNAME));
        return sendMessage(chatId, "Introduzca el nombre de usuario que desee:\n(Use /cancel para cancelar el proceso)");
    }

    private SendMessage handleRegistrationState(long chatId, String messageText) {
        RegistrationData data = registrationData.get(chatId);
        switch (data.state) {

            case AWAITING_USERNAME:
                if (messageText.length() < 7) {
                    return sendMessage(chatId, "El nombre de usuario debe contener más de 7 caracteres. Por favor, intente nuevamente.");
                }
                try {
                    restTemplate.getForEntity(
                        "http://localhost:8080/users/search?username=" + messageText,
                        Map.class
                    );
                    // Si no lanza excepción, significa que el usuario existe
                    return sendMessage(chatId, "El nombre de usuario ya está en uso. Por favor, elija otro nombre de usuario.");
                } catch (HttpClientErrorException.Conflict e) {
                    // Usuario ya existe
                    return sendMessage(chatId, "El nombre de usuario ya está en uso. Por favor, elija otro nombre de usuario.");
                } catch (HttpClientErrorException.NotFound e) {
                    // Usuario no existe, podemos continuar
                    data.username = messageText;
                    data.state = RegistrationState.AWAITING_PASSWORD;
                    return sendMessage(chatId, "Nombre de usuario recibido. Ahora, ingrese su contraseña:");
                } catch (Exception e) {
                    return sendMessage(chatId, "Ocurrió un error al verificar el nombre de usuario. Por favor, intente nuevamente.");
                }

            case AWAITING_PASSWORD:
                if (messageText.length() < 8) {
                    return sendMessage(chatId, "La contraseña debe contener más de 8 caracteres. Por favor, intente nuevamente.");
                }
                data.password = messageText;
                data.state = RegistrationState.AWAITING_PASSWORD_CONFIRMATION;
                return sendMessage(chatId, "Contraseña recibida. Por favor, confirme su contraseña:");

            case AWAITING_PASSWORD_CONFIRMATION:
                if (!messageText.equals(data.password)) {
                    return sendMessage(chatId, "Las contraseñas ingresadas no coinciden. Por favor, ingrese nuevamente su contrasea.");
                }
                data.state = RegistrationState.AWAITING_EMAIL;
                return sendMessage(chatId, "Por favor, introduzca su correo electrónico para la verificación:");
            
            case AWAITING_EMAIL:
                data.email = messageText;
                String verificationToken = generateVerificationToken();
                data.verificationToken = verificationToken;
    
                storeTokenInRedis(data.username, verificationToken);
                sendVerificationEmail(data.email, verificationToken);
    
                data.state = RegistrationState.AWAITING_VERIFICATION_TOKEN;
                return sendMessage(chatId, "Se ha enviado un código de verificación a su correo electrónico. Ingréselo aquí para finalizar el registro:");
    
            case AWAITING_VERIFICATION_TOKEN:
                String storedToken = redisTemplate.opsForValue().get("verificationToken:" + data.username);

                if (storedToken == null || !storedToken.equals(messageText)) {
                    return sendMessage(chatId, "Código de verificación incorrecto o expirado. Por favor, intente nuevamente.");
                }
    
                redisTemplate.delete("verificationToken:" + data.username);
                return registerUser(chatId, data.username, data.password);

            default:
                return sendMessage(chatId, "Se ha producido un error durante el registro. Por favor, inténtelo de nuevo con /register");
        }
    }

    private void sendVerificationEmail(String email, String token) {
        try {
            String subject = "Código de Verificación de Registro";
            String message = "Su código de verificación es: " + token + ". Este código expira en 5 minutos.";
            logger.info("Enviando email a: " + email + " con token: " + token);
            emailService.sendEmail(email, subject, message);
            logger.info("Email enviado exitosamente");
        } catch (Exception e) {
            logger.error("Error enviando email: " + e.getMessage(), e);
            throw e;
        }
    }


    private String generateVerificationToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }

    private void storeTokenInRedis(String username, String token) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set("verificationToken:" + username, token, Duration.ofMinutes(5));
    }

    private SendMessage registerUser(long chatId, String username, String password) {
        RegistrationData data = registrationData.get(chatId);
        CreateUser createUser = new CreateUser(username, password, data.email);

        try {
            restTemplate.postForEntity("http://localhost:8080/users", createUser, Void.class);
            loggedInUsers.put(chatId, username);
            registrationData.remove(chatId);
            return sendMessage(chatId, "¡Registro exitoso! Has iniciado sesión automáticamente.\n" +
                "Recomendamos borrar el chat para no dejar rastros de tu registro.\n" +
                "Para desloguearse use el comando /logout");
        } catch (Exception e) {
            registrationData.remove(chatId);
            return sendMessage(chatId, "Se ha producido un error durante el registro. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    private SendMessage handleHelpCommand(long chatId) {
        StringBuilder helpMessage = new StringBuilder("Comandos disponibles 🤖\n\n");
        helpMessage.append("/help - Mostrar este mensaje de ayuda\n");
        helpMessage.append("/events - Listar todos los eventos disponibles\n");
        helpMessage.append("/search <event name> - Buscar un evento específico por nombre\n");
        helpMessage.append("/eventDetail <event name> - Obtener información detallada sobre un evento específico\n");
        helpMessage.append("/register - Crear una nueva cuenta de usuario\n");
        helpMessage.append("/logout - Cerrar sesión\n");
        helpMessage.append("/login - Iniciar sesión\n");
        helpMessage.append("/reserve - Realizar una reserva de tickets para un evento\n");
        helpMessage.append("/cancel - Cancelar el proceso actual\n");
        helpMessage.append("/myTickets - Ver tus tickets reservados\n");
        if (adminUsers.getOrDefault(chatId, false)) {
            helpMessage.append("\nComandos de Administrador 👑\n");
            helpMessage.append("/createEvent - Crear un nuevo evento\n");
            helpMessage.append("/closeEvent <nombre> - Cerrar un evento específico\n");
        }
        return sendMessage(chatId, helpMessage.toString());
    }

    private SendMessage sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("Markdown");
        return message;
    }

    private SendMessage handleLogoutCommand(long chatId) {
        if (loggedInUsers.containsKey(chatId)) {
            String userId = loggedInUsers.remove(chatId);
            boolean wasAdmin = adminUsers.remove(chatId) != null;
            
            if (wasAdmin) {
                return sendMessage(chatId, "¡Hasta luego admin! Has cerrado sesión exitosamente.");
            } else {
                try {
                    ResponseEntity<Map> response = restTemplate.getForEntity(
                        "http://localhost:8080/users/" + userId,
                        Map.class
                    );
                    String username = (String) response.getBody().get("username");
                    return sendMessage(chatId, "¡Hasta luego " + username + "! Has cerrado sesión exitosamente.");
                } catch (Exception e) {
                    return sendMessage(chatId, "Has cerrado sesión exitosamente.");
                }
            }
        } else {
            return sendMessage(chatId, "No has iniciado sesión.");
        }
    }

    private SendMessage handleLoginCommand(long chatId) {
        if (loggedInUsers.containsKey(chatId)) {
            return sendMessage(chatId, "Ya has iniciado sesión.");
        }
        loginData.put(chatId, new LoginData(LoginState.AWAITING_USERNAME));
        return sendMessage(chatId, "Por favor, ingresa tu nombre de usuario:\n(Use /cancel para cancelar el proceso)");
    }

    private SendMessage handleLoginState(long chatId, String messageText) {
        LoginData data = loginData.get(chatId);
        
        switch (data.state) {
            case AWAITING_USERNAME:
                try {
                    logger.info("Intentando login para usuario: " + messageText);
                    ResponseEntity<Map> response = restTemplate.getForEntity(
                        "http://localhost:8080/users/search?username=" + messageText,
                        Map.class
                    );
                    
                    logger.info("Respuesta del servidor: " + response.getStatusCode() + " - " + response.getBody());
                    
                    if (response.getStatusCode() == HttpStatus.OK) {
                        data.username = messageText;
                        String verificationToken = generateVerificationToken();
                        data.verificationToken = verificationToken;
                        
                        @SuppressWarnings("unchecked")
                        Map<String, Object> userData = (Map<String, Object>) response.getBody();
                        String userEmail = (String) userData.get("email");
                        
                        storeTokenInRedis(data.username, verificationToken);
                        sendVerificationEmail(userEmail, verificationToken);
                        
                        data.state = LoginState.AWAITING_VERIFICATION_CODE;
                        return sendMessage(chatId, "Se ha enviado un código de verificación a tu email. Por favor, ingrésalo:\n(Use /cancel para cancelar el proceso)");
                    }
                } catch (HttpClientErrorException.NotFound e) {
                    logger.error("Usuario no encontrado: " + e.getMessage());
                    loginData.remove(chatId);
                    return sendMessage(chatId, "Usuario no encontrado. Por favor, intenta nuevamente con /login");
                } catch (Exception e) {
                    logger.error("Error inesperado durante login: ", e);
                    loginData.remove(chatId);
                    return sendMessage(chatId, "Error al buscar el usuario. Por favor, intenta nuevamente con /login");
                }

            case AWAITING_VERIFICATION_CODE:
                String storedToken = redisTemplate.opsForValue().get("verificationToken:" + data.username);
                
                if (storedToken == null || !storedToken.equals(messageText)) {
                    loginData.remove(chatId);
                    return sendMessage(chatId, "Código de verificación incorrecto o expirado. Por favor, intenta nuevamente con /login");
                }

                // Si es un intento de login admin
                if ("admin".equals(data.username)) {
                    ResponseEntity<Map> response = restTemplate.getForEntity(
                            "http://localhost:8080/users/search?username=" + messageText,
                            Map.class
                    );

                    String storedAdminToken = redisTemplate.opsForValue().get("verificationToken:admin");
                    
                    if (storedAdminToken != null && storedAdminToken.equals(messageText)) {
                        adminUsers.put(chatId, true);
                        ResponseEntity<Map> userResponse = restTemplate.getForEntity(
                                "http://localhost:8080/users/search?username=" + data.username,
                                Map.class
                        );
                        String userId = userResponse.getBody().get("id").toString();
                        loggedInUsers.put(chatId, userId);
                        redisTemplate.delete("verificationToken:admin");
                        loginData.remove(chatId);
                        return sendMessage(chatId, "¡Inicio de sesión como administrador exitoso! 👑");
                    } else {
                        loginData.remove(chatId);
                        return sendMessage(chatId, "Código de verificación incorrecto o expirado.");
                    }
                }

                ResponseEntity<Map> userResponse = restTemplate.getForEntity(
                    "http://localhost:8080/users/search?username=" + data.username,
                    Map.class
                );
                String userId = userResponse.getBody().get("id").toString();

                redisTemplate.delete("verificationToken:" + data.username);
                loggedInUsers.put(chatId, userId); // Guardamos el ID en lugar del username
                loginData.remove(chatId);
                return sendMessage(chatId, "¡Inicio de sesión exitoso! Bienvenido " + data.username);

            default:
                loginData.remove(chatId);
                return sendMessage(chatId, "Error en el proceso de login. Por favor, intenta nuevamente con /login");
        }
    }

    private SendMessage handleReserveCommand(long chatId) {
        if (!loggedInUsers.containsKey(chatId)) {
            return sendMessage(chatId, "Debes iniciar sesión para realizar una reserva. Usa /login");
        }
        
        reservationData.put(chatId, new ReservationData(ReservationState.AWAITING_EVENT_NAME));
        return sendMessage(chatId, "Por favor, ingresa el nombre del evento que deseas reservar:\n(Use /cancel para cancelar el proceso)");
    }

    private SendMessage handleReservationState(long chatId, String messageText) {
        ReservationData data = reservationData.get(chatId);
        
        switch (data.state) {
            case AWAITING_EVENT_NAME:
                try {
                    ResponseEntity<List<Event>> response = restTemplate.exchange(
                        "http://localhost:8080/events",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Event>>() {}
                    );
                    
                    List<Event> allEvents = response.getBody();
                    if (allEvents == null || allEvents.isEmpty()) {
                        return sendMessage(chatId, "No hay eventos disponibles en este momento.");
                    }

                    // Filtrar eventos que coincidan parcialmente con el nombre ingresado
                    List<Event> matchingEvents = allEvents.stream()
                        .filter(event -> event.getName().toLowerCase()
                            .contains(messageText.toLowerCase()))
                        .collect(Collectors.toList());

                    if (matchingEvents.isEmpty()) {
                        return sendMessage(chatId, "No se encontró ningún evento con ese nombre. Por favor, intenta nuevamente:\n(Use /cancel para cancelar el proceso)");
                    }
                    
                    if (matchingEvents.size() > 1) {
                        StringBuilder message = new StringBuilder();
                        message.append("Se encontraron varios eventos que coinciden con tu búsqueda:\n\n");
                        for (Event event : matchingEvents) {
                            message.append("- ").append(event.getName()).append("\n");
                        }
                        message.append("\nPor favor, ingresa el nombre exacto del evento que deseas reservar:\n(Use /cancel para cancelar el proceso)");
                        return sendMessage(chatId, message.toString());
                    }
                    
                    Event event = matchingEvents.get(0);
                    data.eventId = event.getId();
                    data.eventName = event.getName();
                        
                    if (!event.isOpenSale()) {
                        reservationData.remove(chatId);
                        return sendMessage(chatId, "Lo siento, este evento no tiene las ventas abiertas actualmente.");
                    }

                    StringBuilder locationMessage = new StringBuilder();
                    locationMessage.append("Ubicaciones disponibles para ").append(event.getName()).append(":\n\n");
                    for(Location location : event.getLocations()) {
                        locationMessage.append("- ").append(location.getName()).append(": \n\t")
                                        .append("Precio: $").append(location.getPrice()).append("\n\t")
                                        .append("Tickets disponibles: ").append(location.getQuantityTickets())
                                        .append("\n");
                    }
                    locationMessage.append("\nPor favor, ingresa el nombre de la ubicación deseada:\n(Use /cancel para cancelar el proceso)");
                    
                    data.state = ReservationState.AWAITING_LOCATION;
                    return sendMessage(chatId, locationMessage.toString());
                }
                catch (HttpClientErrorException.NotFound e) {
                    return sendMessage(chatId, "No se encontró ningún evento con ese nombre. Por favor, intenta nuevamente:\n(Use /cancel para cancelar el proceso)");
                }
                catch (Exception e) {
                    return sendMessage(chatId, "Ocurrió un error al buscar el evento. Por favor, intenta nuevamente:\n(Use /cancel para cancelar el proceso)");
                }

            case AWAITING_LOCATION:
                data.locationName = messageText;
                data.state = ReservationState.AWAITING_QUANTITY;
                return sendMessage(chatId, "Por favor, ingresa la cantidad de tickets que deseas reservar:\n(Use /cancel para cancelar el proceso)");

            case AWAITING_QUANTITY:
                try {
                    int quantity = Integer.parseInt(messageText);
                    if (quantity <= 0) {
                        return sendMessage(chatId, "La cantidad debe ser mayor a 0. Por favor, ingresa nuevamente la cantidad:\n(Use /cancel para cancelar el proceso)");
                    }
                    data.quantity = quantity;
                    return createReservation(chatId);
                } catch (NumberFormatException e) {
                    return sendMessage(chatId, "Por favor, ingresa un número válido:\n(Use /cancel para cancelar el proceso)");
                }

            default:
                reservationData.remove(chatId);
                return sendMessage(chatId, "Error en el proceso de reserva. Por favor, intenta nuevamente con /reserve");
        }
    }

    private SendMessage createReservation(long chatId) {
        ReservationData data = reservationData.get(chatId);
        String userId = loggedInUsers.get(chatId);
        CreateReservation reservation = new CreateReservation();
        reservation.setName(data.locationName);
        reservation.setQuantityTickets(data.quantity);

        try {
            restTemplate.postForEntity(
                "http://localhost:8080/events/" + data.eventId + "/reserves?user_id=" + userId,
                reservation,
                Void.class
            );
            reservationData.remove(chatId);
            return sendMessage(chatId, "¡Reserva exitosa! Has reservado " + data.quantity +
                " tickets para " + data.eventName + " en la ubicación " + data.locationName);
        } catch(HttpClientErrorException.NotFound e) {
            logger.error("Error al crear reserva: " + e.getMessage());
            reservationData.remove(chatId);
            return sendMessage(chatId, "La venta del evento fue cerrada.");
        } catch (HttpClientErrorException.BadRequest e) {
            logger.error("Error al crear reserva: " + e.getMessage());
            reservationData.remove(chatId);
            return sendMessage(chatId, "Ya no quedan suficientes tickets para la ubicación elegida. Por favor iniciar nuevamente el proceso de reserva con /reserve.");
        }
        catch (Exception e) {
            logger.error("Error al crear reserva: " + e.getMessage());
            reservationData.remove(chatId);
            return sendMessage(chatId, "Error al realizar la reserva. Por favor, intenta nuevamente con /reserve");
        }
    }

    private SendMessage handleCancelCommand(long chatId) {
        StringBuilder message = new StringBuilder();
        
        if (registrationData.containsKey(chatId)) {
            registrationData.remove(chatId);
            message.append("Proceso de registro cancelado.");
        } else if (loginData.containsKey(chatId)) {
            loginData.remove(chatId);
            message.append("Proceso de login cancelado.");
        } else if (reservationData.containsKey(chatId)) {
            reservationData.remove(chatId);
            message.append("Proceso de reserva cancelado.");
        } else if (createEventData.containsKey(chatId)) {
            createEventData.remove(chatId);
            message.append("Proceso de creación de evento cancelado.");
        } else {
            message.append("No hay ningún proceso activo para cancelar.");
        }
        
        return sendMessage(chatId, message.toString());
    }

    private SendMessage handleAdminCommand(long chatId) {
        if (loggedInUsers.containsKey(chatId)) {
            return sendMessage(chatId, "Ya has iniciado sesión. Cierra sesión primero con /logout");
        }
        
        try {
            // Buscar el usuario admin en la base de datos
            ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://localhost:8080/users/search?username=admin",
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                @SuppressWarnings("unchecked")
                Map<String, Object> adminData = (Map<String, Object>) response.getBody();
                String adminEmail = (String) adminData.get("email");
                
                // Generar y enviar código de verificación
                String verificationToken = generateVerificationToken();
                storeTokenInRedis("admin", verificationToken);
                sendVerificationEmail(adminEmail, verificationToken);
                
                LoginData loginData = new LoginData(LoginState.AWAITING_VERIFICATION_CODE);
                loginData.username = "admin"; // Seteamos explícitamente el username
                this.loginData.put(chatId, loginData);
                
                return sendMessage(chatId, "Se ha enviado un código de verificación al email del administrador.\nPor favor, ingrésalo:\n(Use /cancel para cancelar el proceso)");
            }
            
            return sendMessage(chatId, "Error: Usuario administrador no encontrado en el sistema.");
        } catch (Exception e) {
            logger.error("Error en login de admin: ", e);
            return sendMessage(chatId, "Error al intentar acceder como administrador. Por favor, intente nuevamente.");
        }
    }
}