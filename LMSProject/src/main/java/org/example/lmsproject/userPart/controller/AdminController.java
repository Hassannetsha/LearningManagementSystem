package org.example.lmsproject.userPart.controller;

import org.example.lmsproject.Notification.Services.MailboxService;
// import org.example.lmsproject.userPart.model.Admin;
import org.example.lmsproject.userPart.model.Request;
import org.example.lmsproject.userPart.model.Response;
import org.example.lmsproject.userPart.model.ResponseNotification;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.repository.RequestRepository;
import org.example.lmsproject.userPart.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    MailboxService mailboxService;

    @Autowired
    RequestRepository requestRepository;



    @PostMapping("/addUser")
    public String addNewUser(@RequestBody User userRequest) {
        User user = adminService.createUserByRole(userRequest);
        String response = adminService.addUser(user);
        return user.getUsername() + " Added Successfully\n" + response;
    }

    @PostMapping("/responses")
    public String processAllResponses(@RequestBody int state) {
        List<Request> allRequests = requestRepository.findAll(); // Fetch all requests
        StringBuilder result = new StringBuilder();

        for (Request request : allRequests) {
            if (state == 1) { // Approved state
                User new_user = new User();
                new_user.setEmail(request.getEmail());
                new_user.setUsername(request.getUsername());
                new_user.setRole(request.getRole());
                new_user.setPassword(request.getPassword());
                User user = adminService.createUserByRole(new_user);
                String responseMessage = adminService.addUser(user);
                requestRepository.delete(request);
                // Notification Logic
                mailboxService.addNotification(user.getId(), new ResponseNotification());

                result.append(user.getUsername()).append(" Added Successfully\n").append(responseMessage).append("\n");
            } else { // Rejected state
                result.append("Request with ID ").append(request.getId()).append(" was not approved.\n");
            }
        }

        return result.toString();
    }


    @PostMapping("/response")
    public String response(@RequestBody Response response) {
        System.out.println(" ID "+response.getId());
        Request request = adminService.getRequestByID(response.getId());
        if (request!=null) {
            if (response.getState()==1) {
                User new_user = new User();
                new_user.setEmail(request.getEmail());
                new_user.setUsername(request.getUsername());
                new_user.setRole(request.getRole());
                new_user.setPassword(request.getPassword());
                User user = adminService.createUserByRole(new_user);
                String responseMessage = adminService.addUser(user);
                requestRepository.delete(request);
                // added Notification Logic //////////////////////////////////////////////////////////////////////////
                // NotificationAndEmailMapper responsNotification = new ResponseNotification();
                mailboxService.addNotification(user.getId(), new ResponseNotification());

                /////////////////////////////////////////////////////////////////////////////////////////////////////

                return user.getUsername() + " Added Successfully\n" + responseMessage;
            }
            else{
                return "User Not Added";
            }
        }
        else{
            return "Request Not Found";
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        String response = adminService.updateUser(id, updatedUser);
        return ResponseEntity.ok(response);
    }

    // Endpoint for deleting a User
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = adminService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}
