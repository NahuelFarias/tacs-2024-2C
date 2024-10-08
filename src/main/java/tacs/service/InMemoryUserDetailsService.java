package tacs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tacs.config.CustomUserDetails;
import tacs.models.domain.users.NormalUser;
import tacs.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class InMemoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NormalUser normalUser = userRepository.findByUsername(username);
        if (normalUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        //System.out.println("User found: " + normalUser.getUsername() + ", Role: " + normalUser.getRol().getNombre());
        return new CustomUserDetails(normalUser);
    }
}