package tacs.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tacs.models.domain.users.NormalUser;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private NormalUser normalUser;

    public CustomUserDetails(NormalUser normalUser) {
        this.normalUser = normalUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Suponemos, que el rol asociado a un usuario es UNICO
        return List.of(new SimpleGrantedAuthority("ROLE_" + normalUser.getRol().getNombre()));
    }

    @Override
    public String getPassword() {
        return normalUser.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return normalUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
