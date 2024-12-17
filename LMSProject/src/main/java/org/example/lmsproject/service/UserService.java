package org.example.lmsproject.service;

import org.example.lmsproject.model.User;
import org.example.lmsproject.model.Admin;
import org.example.lmsproject.model.Instructor;
import org.example.lmsproject.model.Student;
import org.example.lmsproject.model.User_Details;
import org.example.lmsproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// import java.util.Collection;
import java.util.List;
import java.util.Optional;
// import java.util.Optional;

@Service
public class UserService implements UserDetailsService {// service hia elly f el nos bttklm m3 el repo w el
    // api(conroller)

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;

    @Override
    // load user by ID
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Long userId = Long.parseLong(id);// Parse the id from the input string
            // System.out.println("ID==" + userId);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));
            System.out.println(user.getUsername());
            System.out.println("&&&" + user.getPassword());
            return new User_Details(user); // Or return user if it implements UserDetails\

        } catch (NumberFormatException e) {
            System.out.println("ERROR");
            throw new UsernameNotFoundException("Invalid id format: " + username);
        }

    }

    // public String addUser(User user) {
    // System.out.println(user.getUsername());
    // user.setPassword(encoder.encode(user.getPassword()));
    // if (user instanceof Admin) {
    // user.setRole(User.Role.ROLE_ADMIN);
    // System.out.println("Adding Admin: " + user.getUsername());
    // } else if (user instanceof Instructor) {
    // user.setRole(User.Role.ROLE_INSTRUCTOR);
    // System.out.println("Adding Instructor: " + user.getUsername());
    // } else if (user instanceof Student) {
    // user.setRole(User.Role.ROLE_STUDENT);
    // System.out.println("Adding Student: " + user.getUsername());
    // } else {
    // System.out.println("Adding a generic user: " + user.getUsername());
    // }
    // // Save the user to the repository (JPA handles polymorphism)
    // try {
    // userRepository.save(user);
    // } catch (Exception e) {
    // System.err.println("Error saving user: " + e.getMessage());
    // e.printStackTrace();
    // }
    // return user.getClass().getSimpleName() + " Added Successfully";
    // }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public String updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            // Update fields
            if (updatedUser.getUsername() != null) {
                existingUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(encoder.encode(updatedUser.getPassword()));
            }
            // Save the updated user back to the database
            userRepository.save(existingUser);
            return "User updated successfully: " + existingUser.getUsername();
        } else {
            return "User with ID " + id + " not found.";
        }
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String deleteUser(Long id) {
        User user = getUser(id);
        if (user != null) {
            userRepository.delete(user);
            return "User deleted successfully: " + user.getUsername();
        } else {
            return "User with ID " + id + " not found.";
        }
    }

    public User createUserByRole(User user) {
        return switch (user.getRole()) {
            case ROLE_ADMIN -> new Admin(user.getUsername(), user.getPassword(), user.getEmail());
            case ROLE_INSTRUCTOR -> new Instructor(user.getUsername(), user.getPassword(), user.getEmail());
            case ROLE_STUDENT -> new Student(user.getUsername(), user.getPassword(), user.getEmail());
            default -> throw new IllegalArgumentException("Invalid role: " + user.getRole());
        };
    }

    public String addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User added successfully";
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