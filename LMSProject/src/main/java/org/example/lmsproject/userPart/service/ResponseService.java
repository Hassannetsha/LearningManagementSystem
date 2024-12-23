package org.example.lmsproject.userPart.service;

import jakarta.transaction.Transactional;
import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.userPart.model.Request;
import org.example.lmsproject.userPart.model.Response;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    private final RequestRepository requestRepository;
    private final AdminService adminService;
    private final MailboxService mailboxService;

    public ResponseService(RequestRepository requestRepository, AdminService adminService, MailboxService mailboxService) {
        this.requestRepository = requestRepository;
        this.adminService = adminService;
        this.mailboxService = mailboxService;
    }

    public String processAllResponses(int state) {
        List<Request> allRequests = requestRepository.findAll(); // Fetch all requests
        StringBuilder result = new StringBuilder();

        for (Request request : allRequests) {
            Response response = new Response();
            response.setId(request.getId());
            response.setState(state);

            String message = processResponse(response); // Process each request
            result.append(message).append("\n");
        }

        return result.toString();
    }

    public String processResponse(Response response) {
        Request request = adminService.getRequestByID(response.getId());
        if (request == null) {
            return "Request Not Found";
        }
        if (response.getState() == 1) {
            User newUser = createUserFromRequest(request);
            String responseMessage = adminService.addUser(newUser);
            requestRepository.delete(request);
            mailboxService.addNotification(newUser.getId(), response);

            return newUser.getUsername() + " Added Successfully\n" + responseMessage;
        } else {
            return "Request Rejected: User Not Added";
        }
    }

    private User createUserFromRequest(Request request) {
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setRole(request.getRole());
        newUser.setPassword(request.getPassword());
        return adminService.createUserByRole(newUser);
    }
}
