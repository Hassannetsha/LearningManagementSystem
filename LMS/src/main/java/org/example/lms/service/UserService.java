package org.example.lms.service;

import org.example.lms.model.User;
import org.example.lms.model.User_Details;
import org.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// import java.util.Collection;
import java.util.List;
// import java.util.Optional;

@Service
public class UserService implements UserDetailsService {// service hia elly f el nos bttklm m3 el repo w el
                                                        // api(conroller)
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    // load user by ID
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        try {
            Long userId = Long.parseLong(id); // Parse the id from the input string
            User user = userRepository.findUserById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
            
            // Log user details for debugging
            System.out.println("User found: " + user.getUsername() + ", Role: " + user.getRole());

            return new User_Details(user); // Use your User_Details class
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid id format: " + id);
        }
    }

    public String addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User Added Successfully";
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // public void addInitialUser() {
    // User user = new User();
    // user.setUsername("admin"); // Set the username
    // user.setPassword("password123"); // Set the password (will be encoded)
    // user.setRole("ROLE_ADMIN"); // Set the role (can be "ROLE_ADMIN",
    // "ROLE_STUDENT", etc.)
    // addUser(user); // Save the user to the database
    // }
}