package tacs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tacs.dto.JWT;
import tacs.dto.LoginRequest;
import tacs.models.domain.users.NormalUser;
import tacs.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class AutenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public JWT login(LoginRequest loginRequest) {

        NormalUser normalUser = this.userRepository.findByUsername(loginRequest.getUsername());

        if (normalUser == null || !passwordEncoder.matches(loginRequest.getPassword(), normalUser.getHashedPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        normalUser.setLastLogin(LocalDateTime.now());
        this.userRepository.save(normalUser);

        return new JWT("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiZXhhbXBsZS1qd3QifQ.JYF_kuzaikLAIQbRCNe_wA-B7yZZKYp0jOwBUazOFKM",
                normalUser.getId(), normalUser.getRol().getNombre());
    }

    public String getSalt(String username) {
        NormalUser normalUser = this.userRepository.findByUsername(username);
        String[] parts = normalUser.getHashedPassword().split(":");
        return parts[0];
    }
}
