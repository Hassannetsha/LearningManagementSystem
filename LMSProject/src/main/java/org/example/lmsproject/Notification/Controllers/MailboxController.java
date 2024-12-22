package org.example.lmsproject.Notification.Controllers;

import java.util.List;

import org.example.lmsproject.Notification.Entities.Mailbox;
import org.example.lmsproject.Notification.Entities.Notification;
import org.example.lmsproject.Notification.Services.MailboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// import project_software.main.Notification.Entities.Mailbox;
// import project_software.main.Notification.Entities.Notification;
// import project_software.main.Notification.Services.MailboxService;
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

    @GetMapping(path = "{mailboxID}")
    public Mailbox  getMailbox(@PathVariable("mailboxID") Long id) {
        return mailboxService.getMailbox(id);
    }

    @GetMapping(path = "{userID}")
    public List<Notification> getNotifications(@PathVariable("userID") Long id) {
        return mailboxService.getNotifications(id);
    }

    @PostMapping
    public void addNotification(@RequestParam Long userId, @RequestParam Object message) {
        mailboxService.addNotification(userId, message);
    }

    @PostMapping(path = "bulk")
    public void addBulkNotifications(@RequestParam List<Long> userIDs, @RequestParam String message) {
        mailboxService.addBulkNotifications(userIDs, message);
    }

    @PutMapping(path = "markAllAsRead/{userId}")
    public void markAllAsRead(@PathVariable("userId") Long id) {
        mailboxService.markAllAsRead(id);
    }

    @PutMapping(path = "markAllAsUnread/{userId}")
    public void markAllAsUnread(@PathVariable("userId") Long id) {
        mailboxService.markAllAsUnread(id);
    }

    // @DeleteMapping(path = "delete/{notificationId}")
    // public void deleteNotification(@PathVariable("notificationId") Long id) {
    //     mailboxService.deleteNotification(id);
    // }

    @DeleteMapping(path = "clear/{userID}")
    public void clearNotifications(@PathVariable("userID") Long id) {
        mailboxService.clearNotifications(id);
    }

}
