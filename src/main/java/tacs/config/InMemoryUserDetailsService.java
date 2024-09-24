package tacs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tacs.models.domain.users.NormalUser;
import tacs.repository.UserRepository;


@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public InMemoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NormalUser normalUser = userRepository.findByUsername(username);
        if (normalUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(normalUser);
    }
}