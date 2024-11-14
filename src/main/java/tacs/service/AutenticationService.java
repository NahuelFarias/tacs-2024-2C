package tacs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tacs.dto.JWT;
import tacs.dto.LoginRequest;
import tacs.models.domain.users.NormalUser;
import tacs.repository.UserRepository;
import tacs.security.JwtTokenProvider;

import java.time.LocalDateTime;

@Service
public class AutenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    @Autowired
    public AutenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                                AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public JWT login(LoginRequest loginRequest) {
        NormalUser normalUser = this.userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = this.authenticationManager.authenticate(authToken);

            normalUser.setLastLogin(LocalDateTime.now());
            this.userRepository.save(normalUser);

            JWT response =  new JWT(jwtTokenProvider.generateToken(normalUser), normalUser.getId(), normalUser.getRol().getNombre());
            return response;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public String getSalt(String username) {
        NormalUser normalUser = this.userRepository.findByUsername(username)
            .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        String[] parts = normalUser.getHashedPassword().split(":");
        return parts[0];
    }
}
