package tacs.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tacs.models.domain.users.NormalUser;
import tacs.models.domain.users.Rol;
import tacs.repository.UserRepository;

@Component
public class DataInitializer {

    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
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
}
