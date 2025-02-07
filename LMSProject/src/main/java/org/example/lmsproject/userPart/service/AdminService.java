package org.example.lmsproject.userPart.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.lmsproject.Notification.Repositories.MailboxRepository;
import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.example.lmsproject.Notification.TextMappers.RequestNotification;
import org.example.lmsproject.userPart.model.Admin;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.Request;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.User;
import static org.example.lmsproject.userPart.model.User.Role.ROLE_ADMIN;
import org.example.lmsproject.userPart.repository.RequestRepository;
import org.example.lmsproject.userPart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final  PasswordEncoder encoder;
    private final UserRepository userRepo;
    private final RequestRepository requestRepo;
    private final MailboxRepository mailboxRepository;
    // added for Notification Logic (& added in constructor)
    private final MailboxService mailboxService;
    //
    @Autowired
    public AdminService(PasswordEncoder encoder, UserRepository userRepo, RequestRepository requestRepo, MailboxRepository mailboxRepository, MailboxService mailboxService) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
        this.mailboxRepository = mailboxRepository;
        this.mailboxService = mailboxService;
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
            mailboxRepository.deleteById(user.getId());

            userRepo.delete(user);
            return "User deleted successfully: " + user.getUsername();
        } else {
            return "User with ID " + id + " not found.";
        }
    }


    public void sendRequest(Request userRequest) {
        System.out.println(userRequest.getUsername());
        if (userRequest.getUsername()!=null&&userRequest.getPassword()!=null&&userRequest.getEmail()!=null&&userRequest.getRole()!=null) {
            if (userRepo.findByusername(userRequest.getUsername()).isPresent()){
                throw new IllegalArgumentException("Username already exists");
            }
            requestRepo.save(userRequest);
            // added Notification Logic //////////////////////////////////////////////////////////////////////////
            List<Long> adminUserIds = getAllAdmins().stream()
                    .map(Admin::getId)
                    .toList();
            if (adminUserIds.isEmpty()) { throw new IllegalStateException("No Admin Level Users Found"); }
            NotificationAndEmailMapper requestNotification = new RequestNotification(userRequest);
            mailboxService.addBulkNotifications(adminUserIds, requestNotification);
            /////////////////////////////////////////////////////////////////////////////////////////////////////

        }
        else{
            throw new IllegalArgumentException("Invalid request");
        }
        // notificationController
    }


    // added FOR Notification Logic //////////////////////////////////////////////////////////////////////////

    public List<Admin> getAllAdmins(){
        return userRepo.findAll().stream().filter(u->u.getRole().equals(ROLE_ADMIN)).map(Admin.class::cast).collect(Collectors.toList());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Request> getRequests() {
        return requestRepo.findAll();
    }
    public Request getRequestByID(Long id) {
        return requestRepo.findByid(id);
    }

}
