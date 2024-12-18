package project_software.main.Notification.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project_software.main.Notification.Entities.Notification;
import project_software.main.Notification.Services.NotificationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getNotifications() {
        return notificationService.getNotifications();
    }

    @GetMapping(path = "{userID}")
    public List<Notification> getNotificationsByUserID(@PathVariable("userID") long id) {
        return notificationService.getNotificationsByUserID(id);
    }

    @PostMapping
    public void postNotification(@RequestBody Notification notification) {
        notificationService.postNotification(notification);
    }

    @PostMapping(path = "bulk")
    public void postBulkNotifications(@RequestParam List<Long> userIDs, @RequestParam String message) {
        notificationService.postBulkNotifications(userIDs, message);
    }

    @DeleteMapping(path = "delete/{notificationID}")
    public void deleteNotification(@PathVariable("notificationID") long id) {
        notificationService.deleteNotification(id);
    }

    @PutMapping(path = "markAsRead/{notificationID}")
    public void markAsRead(@PathVariable("notificationID") long id) {
        notificationService.markAsRead(id);
    }

    @PutMapping(path = "markAllAsRead/{userID}")
    public void markAllAsRead(@PathVariable("userID") long id) {
        notificationService.markAllAsRead(id);
    }

    @PutMapping(path = "markAsUnread/{notificationID}")
    public void markAsUnread(@PathVariable("notificationID") long id) {
        notificationService.markAsUnread(id);
    }
}
