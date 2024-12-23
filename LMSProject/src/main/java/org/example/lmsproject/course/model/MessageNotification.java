package org.example.lmsproject.course.model;

import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageNotification implements NotificationAndEmailMapper {
    private String message;

    // public MessageNotification(String message) {
    //     this.message = message;
    // }
    // public MessageNotification() {
    //     message = "";
    // }

    @Override
    public String getSubject() {
        return "Notification";
    }

    @Override
    public String getBody() {
        return message;
    }

}
