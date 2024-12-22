package org.example.lmsproject.userPart.service;

import org.example.lmsproject.userPart.model.Admin;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.repository.RequestRepository;
import org.example.lmsproject.userPart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.example.lmsproject.userPart.model.Request;

@Service
public class AdminService {
    private final  PasswordEncoder encoder;
    private final UserRepository userRepo;
    private final RequestRepository requestRepo;

    @Autowired
    public AdminService(PasswordEncoder encoder, UserRepository userRepo,RequestRepository requestRepo) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
    }


    public User createUserByRole(User user) {
        if (user==null){
            throw new IllegalArgumentException("User cannot be null");
        }
        System.out.println(user.getUsername()+" " + user.getRole());
        return switch (user.getRole()) {
            case ROLE_ADMIN -> new Admin(user.getUsername(), user.getPassword(), user.getEmail());
            case ROLE_INSTRUCTOR -> new Instructor(user.getUsername(), user.getPassword(), user.getEmail());
            case ROLE_STUDENT -> new Student(user.getUsername(), user.getPassword(), user.getEmail());
            default -> throw new IllegalArgumentException("Invalid role: " + user.getRole());
        };
    }

    public String addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
        return "User added successfully";
    }


    public String updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepo.findById(id);
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
            userRepo.save(existingUser);
            return "User updated successfully: " + existingUser.getUsername();
        } else {
            return "User with ID " + id + " not found.";
        }
    }

    public String deleteUser(Long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            userRepo.delete(user);
            return "User deleted successfully: " + user.getUsername();
        } else {
            return "User with ID " + id + " not found.";
        }
    }


    public void sendRequest(Request userRequest) {
        if (userRequest.getUsername()!=null&&userRequest.getPassword()!=null&&userRequest.getEmail()!=null&&userRequest.getRole()!=null) {
            requestRepo.save(userRequest);
        }
        else{
            throw new IllegalArgumentException("Invalid request");
        }
        
        // notificationController
    }


    public List<Request> getRequests() {
        return requestRepo.findAll();
    }
    public Request getRequestByID(Long id) {
        return requestRepo.findByid(id);
    }

}
