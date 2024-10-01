package tacs.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tacs.models.domain.events.Location;
import tacs.models.domain.users.NormalUser;
import tacs.models.domain.users.Rol;
import tacs.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer {

    private final UserRepository userRepository;


    @Autowired
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        this.generate_users();
        this.generate_initial_data();
    }

    private void generate_users() {
        String encodedPassword1 = new CustomPBKDF2PasswordEncoder().encode("password123123");
        String encodedPassword2 = new CustomPBKDF2PasswordEncoder().encode("admin123123");

        NormalUser user = new NormalUser();
        user.setUsername("user");
        user.setHashedPassword(encodedPassword1);
        user.setRol(new Rol("ROLE_USER"));

        NormalUser admin = new NormalUser();
        admin.setUsername("admin");
        admin.setHashedPassword(encodedPassword2);
        admin.setRol(new Rol("ROLE_ADMIN"));

        userRepository.save(user);
        userRepository.save(admin);
    }

    private void generate_initial_data() {
        Location preferencia = new Location("Preferencia",500,12);
        Location eastStand = new Location("East Stand", 200, 20);
        Location tribunaNorte = new Location("Tribuna Norte", 400, 17);
        Location gradaSur = new Location("Grada Sur", 100, 100);

        List<Location> testLocations = new ArrayList<>(Arrays.asList(preferencia,eastStand,tribunaNorte,gradaSur));


    }
}
