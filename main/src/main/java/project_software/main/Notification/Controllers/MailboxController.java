package project_software.main.Notification.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project_software.main.Notification.Entities.Notification;
import project_software.main.Notification.Services.MailboxService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "api/mailbox")
public class MailboxController {

    private final MailboxService mailboxService;

    @Autowired
    public MailboxController(MailboxService mailboxService) {
        this.mailboxService = mailboxService;
    }

    @GetMapping(path = "{userID}")
    public List<Notification> getNotificationsByUserId(@PathVariable("userID") Long id) {
        return mailboxService.getNotificationsByUserId(id);
    }

    @PostMapping
    public void postNotification(@RequestParam Long userId, @RequestParam String message) {
        mailboxService.postNotificationToMailbox(userId, message);
    }

    @PutMapping(path = "markAllAsRead/{userId}")
    public void markAllAsRead(@PathVariable("userId") Long id) {
        mailboxService.markAllNotificationsAsRead(id);
    }

    @PutMapping(path = "markAllAsUnread/{userId}")
    public void markAllAsUnread(@PathVariable("userId") Long id) {
        mailboxService.markAllNotificationsAsUnread(id);
    }

    @DeleteMapping(path = "delete/{notificationId}")
    public void deleteNotification(@PathVariable("notificationId") Long id) {
        mailboxService.deleteNotification(id);
    }

    @DeleteMapping(path = "clear/{userID}")
    public void clearNotifications(@PathVariable("userID") Long id) {
        mailboxService.clearNotifications(id);
    }

}
