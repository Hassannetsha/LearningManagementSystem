package org.example.lmsproject.userPart.service;

import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.model.User_Details;
import org.example.lmsproject.userPart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// import java.util.Collection;
import java.util.List;
// import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    final private UserRepository userRepository;
    final private PasswordEncoder encoder;
    final private JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByusername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));
            System.out.println(user.getUsername());
            System.out.println("&&&" + user.getPassword());
            return new User_Details(user);

        } catch (NumberFormatException e) {
            System.out.println("ERROR");
            throw new UsernameNotFoundException("Invalid id format: " + username);
        }

    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String verify(User user) {
        UserDetails userDetails = loadUserByUsername(user.getUsername());

        // Compare raw password with encoded password
        if (encoder.matches(user.getPassword(), userDetails.getPassword())) {
            return jwtService.generateToken(user.getUsername());
        } else {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }
}