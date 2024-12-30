package org.example.lmsproject.Notification.TextMappers;

import org.example.lmsproject.userPart.model.Request;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class RequestNotification implements NotificationAndEmailMapper{
    private Request request;

    @Override
    public String getSubject() {
        return "New User Request Submitted";
    }

    @Override
    public String getBody() {
        return String.format("There is a new request:\nUsername: %s\nEmail: %s\nRole Requested: %s.",
                    request.getUsername(),
                    request.getEmail(),
                    request.getRole());
    }
}
