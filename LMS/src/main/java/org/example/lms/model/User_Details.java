package org.example.lms.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class User_Details implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public User_Details(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        // Ensure roles are prefixed with "ROLE_" as required by Spring Security
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        System.out.println(user.getUsername() + "     " + password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Update logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Update logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Update logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Update logic if needed
    }
}
