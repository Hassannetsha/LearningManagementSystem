package org.example.lmsproject.userPart.configrations;

import org.example.lmsproject.userPart.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtService, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(CsrfConfigurer::disable) // Disable CSRF using the new method
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // Allow all API endpoints
                        .requestMatchers("/instructor/**").hasRole("INSTRUCTOR") // Only ADMIN can access "/admin"
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Only ADMIN can access "/addAdmin"
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .requestMatchers("/api/**").hasAnyRole("INSTRUCTOR", "ADMIN","STUDENT")
                        .anyRequest().authenticated() // Require authentication for other requests
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // @Bean
    // public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder
    // passwordEncoder) throws Exception {
    // AuthenticationManagerBuilder builder =
    // http.getSharedObject(AuthenticationManagerBuilder.class);
    // builder.userDetailsService(userDetailsService)
    // .passwordEncoder(passwordEncoder);
    // return builder.build();
    // }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
