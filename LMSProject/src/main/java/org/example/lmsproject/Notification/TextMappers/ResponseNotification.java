package org.example.lmsproject.Notification.TextMappers;

public class ResponseNotification implements NotificationAndEmailMapper{
    @Override
    public String getSubject() {
        return "User Authorization Success";
    }

    @Override
    public String getBody() {
        return "Congratulations! You have been successfully authorized as a user in the LMS.";
    }
    
}
