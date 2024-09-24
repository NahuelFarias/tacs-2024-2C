package tacs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import tacs.dto.JWT;
import tacs.dto.LoginRequest;
import tacs.models.domain.users.NormalUser;
import tacs.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class AutenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public JWT login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword()));
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }

        NormalUser normalUser = this.userRepository.findByUsername(loginRequest.getUsername());
        normalUser.setLastLogin(LocalDateTime.now());
        this.userRepository.save(normalUser);

        return new JWT("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiZXhhbXBsZS1qd3QifQ.JYF_kuzaikLAIQbRCNe_wA-B7yZZKYp0jOwBUazOFKM",
                normalUser.getId(), normalUser.getRol().getNombre());
    }
}
