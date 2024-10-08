package tacs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import tacs.security.CustomPBKDF2PasswordEncoder;
import tacs.security.JwtAuthenticationFilter;
import tacs.service.InMemoryUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConf {

    private final InMemoryUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConf(InMemoryUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

        return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("*/swagger-ui/**","/v3/api-docx/**","/login/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/statistics/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService);
        authProvider.setPasswordEncoder(this.passwordEncoder());
        http.authenticationProvider(authProvider);
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPBKDF2PasswordEncoder();
    }

}
