package tacs.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tacs.models.domain.events.Event;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;
import tacs.models.domain.users.Rol;
import tacs.repository.EventRepository;
import tacs.repository.TicketRepository;
import tacs.repository.UserRepository;
import tacs.security.CustomPBKDF2PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, EventRepository eventRepository,
                           TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostConstruct
    public void init() {
        this.generate_users();
        this.generate_initial_data();
    }

    private void generate_users() {
        String encodedPassword1 = new CustomPBKDF2PasswordEncoder().encode("password123123");
        String encodedPassword2 = new CustomPBKDF2PasswordEncoder().encode("admin123123");

        NormalUser user = new NormalUser("user");
        user.setHashedPassword(encodedPassword1);
        user.setRol(new Rol("USER"));
        user.setEmail("aysantos@frba.utn.edu.ar");

        NormalUser admin = new NormalUser("admin");
        admin.setHashedPassword(encodedPassword2);
        admin.setRol(new Rol("ADMIN"));
        admin.setEmail("aysantos@frba.utn.edu.ar");

        if (userRepository.findByUsername(admin.username).isEmpty()) {
            userRepository.save(admin);
        }
        if (userRepository.findByUsername(user.username).isEmpty()) {
            userRepository.save(user);
        }
    }

    public void generate_initial_data() {
        Location preferencia = new Location("Preferencia",500,12);
        preferencia.setId(UUID.randomUUID().toString());
        Location eastStand = new Location("East Stand", 200, 20);
        eastStand.setId(UUID.randomUUID().toString());
        Location tribunaNorte = new Location("Tribuna Norte", 400, 17);
        tribunaNorte.setId(UUID.randomUUID().toString());
        Location gradaSur = new Location("Grada Sur", 100, 100);
        gradaSur.setId(UUID.randomUUID().toString());

        String someImage = "https://www.unidiversidad.com.ar/cache/bc764704c45badb463645914de89d182_1000_1100.jpg";

        List<Location> testLocations = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));
        Event eventoTest = new Event("River vs Boca", LocalDate.of(2018, Month.DECEMBER, 9).atStartOfDay(),someImage);
        eventoTest.setLocations(testLocations);


        Location preferencia2 = new Location("Preferencia",500,13);
        preferencia2.setId(UUID.randomUUID().toString());
        Location eastStand2 = new Location("East Stand", 200, 53);
        eastStand2.setId(UUID.randomUUID().toString());
        Location tribunaNorte2 = new Location("Tribuna Norte", 400, 22);
        tribunaNorte2.setId(UUID.randomUUID().toString());
        Location gradaSur2 = new Location("Grada Sur", 100, 100);
        gradaSur2.setId(UUID.randomUUID().toString());

        List<Location> testLocations2 = new ArrayList<>(Arrays.asList(preferencia2,eastStand2,tribunaNorte2,gradaSur2));
        Event eventoTest2 = new Event("Recital Generico", LocalDate.of(2017, Month.SEPTEMBER, 23).atStartOfDay(), someImage);
        eventoTest2.creationDate = LocalDate.of(2024, Month.FEBRUARY, 9).atStartOfDay();
        eventoTest2.setLocations(testLocations2);


        Location preferencia3 = new Location("Preferencia",500,45);
        preferencia3.setId(UUID.randomUUID().toString());
        Location eastStand3 = new Location("East Stand", 200, 76);
        eastStand3.setId(UUID.randomUUID().toString());
        Location tribunaNorte3 = new Location("Zona Super Random", 400, 23);
        tribunaNorte3.setId(UUID.randomUUID().toString());

        List<Location> testLocations3 = new ArrayList<>(Arrays.asList(preferencia3,eastStand3,tribunaNorte3));
        Event eventoTest3 = new Event("Coldplay", LocalDate.of(2022, Month.NOVEMBER, 11).atStartOfDay(),someImage);
        eventoTest3.creationDate = LocalDate.of(2024, Month.SEPTEMBER, 2).atTime(0,1);
        eventoTest3.setLocations(testLocations3);

        Location preferencia4 = new Location("Preferencia",500,2342);
        preferencia4.setId(UUID.randomUUID().toString());
        Location eastStand4 = new Location("East Stand", 200, 3546);
        eastStand4.setId(UUID.randomUUID().toString());
        Location tribunaNorte4 = new Location("Tribuna Norte", 400, 3435);
        tribunaNorte4.setId(UUID.randomUUID().toString());
        Location gradaSur4 = new Location("Grada Sur", 100, 5555);
        gradaSur4.setId(UUID.randomUUID().toString());

        List<Location> testLocations4 = new ArrayList<>(Arrays.asList(preferencia4,eastStand4,tribunaNorte4,gradaSur4));
        Event eventoTest4 = new Event("Otro evento", LocalDate.of(2019, Month.DECEMBER, 2).atStartOfDay(),someImage);
        eventoTest4.creationDate = LocalDate.of(2024, Month.AUGUST, 24).atTime(11,0);
        eventoTest4.setLocations(testLocations4);
        
        List<Event> testEvents = new ArrayList<>(Arrays.asList(eventoTest,eventoTest2,eventoTest3,eventoTest4));

        testEvents.stream()
                .filter(e -> eventRepository.findByNormalizedName(e.getName()).isEmpty())
                .forEach(eventRepository::save);

        String encodedPassword2 = new CustomPBKDF2PasswordEncoder().encode("contrasenia123");
        NormalUser usuarioTest= new NormalUser("otrousuario");
        usuarioTest.setHashedPassword(encodedPassword2);

        usuarioTest.setLastLogin(LocalDateTime.now());

        // usuarioTest.bookTicket(eventoTest,preferencia);
        // usuarioTest.bookTicket(eventoTest,eastStand);
        // usuarioTest.bookTicket(eventoTest,eastStand);
        // usuarioTest.bookTicket(eventoTest2,eastStand);

        // this.userRepository.saveAndFlush(usuarioTest);
        // this.ticketRepository.saveAllAndFlush(usuarioTest.getTicketsOwned());
    }
}
