package org.example.lmsproject.Notification.Services;

import java.util.List;
import java.util.Optional;

import org.example.lmsproject.Notification.Entities.Mailbox;
import org.example.lmsproject.Notification.Entities.Notification;
import org.example.lmsproject.Notification.Repositories.MailboxRepository;
import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailboxService {
    private final MailboxRepository mailboxRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public MailboxService(MailboxRepository mailboxRepository,
            NotificationService notificationService,
            EmailService emailService,
            UserService userService) {
        this.mailboxRepository = mailboxRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.userService = userService;
    }

    public Mailbox getMailbox(Long mailboxId) {
        return mailboxRepository.findById(mailboxId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for ID " + mailboxId));
    }

    public List<Notification> getNotifications(Long userId) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        return mailbox.getNotifications();
    }

    public void addNotification(Long userId, NotificationAndEmailMapper notificationAndEmailMapper) {
        Optional<Mailbox> mailboxOptional = mailboxRepository.findByUserId(userId);

        Mailbox mailbox;
        if (mailboxOptional.isEmpty()) {
            System.out.println(userId);
            User user = userService.getUser(userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found for ID: " + userId);
            }

            mailbox = new Mailbox(user);
            mailboxRepository.save(mailbox);
        } else {
            mailbox = mailboxOptional.get();
        }

        notificationService.createNotification(mailbox, notificationAndEmailMapper);

        String email = mailbox.getUser().getEmail();
        String subject = notificationAndEmailMapper.getSubject();
        String body = notificationAndEmailMapper.getBody();
        emailService.sendEmail(email, subject, body);
    }

    public void addBulkNotifications(List<Long> userIds, NotificationAndEmailMapper notificationAndEmailMapper) {
        userIds.forEach(userId -> {
            Optional<Mailbox> mailboxID = mailboxRepository.findByUserId(userId);
            // .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID "
            // + userId));
            Mailbox mailbox;
            if (mailboxID.isEmpty()) {
                User user = userService.getUser(userId);
                if (user == null) {
                    throw new IllegalArgumentException("User not found for ID: " + userId);
                }
                mailbox = new Mailbox(user);
                mailboxRepository.save(mailbox);
            } else {
                mailbox = mailboxID.get();
            }
            notificationService.createNotification(mailbox, notificationAndEmailMapper);

            String email = mailbox.getUser().getEmail();
            String subject = notificationAndEmailMapper.getSubject();
            String body = notificationAndEmailMapper.getBody();
            emailService.sendEmail(email, subject, body);
        });
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        mailbox.getNotifications().forEach(notification -> notification.setIsRead(true));
    }

    // @Transactional
    // public void markAllNotificationsAsRead(Long userId) {
    // Mailbox mailbox = mailboxRepository.findByUserId(userId)
    // .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID "
    // + userId));
    // mailbox.getNotifications().forEach(notification -> {
    // if (!notification.getIsRead()) {
    // notification.setIsRead(true);
    // }
    // });
    // }

    @Transactional
    public void markAllAsUnread(Long userId) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        mailbox.getNotifications().forEach(notification -> notification.setIsRead(false));
    }

    // public void deleteNotification(Long notificationId) {
    // Notification notification = notificationRepository.findById(notificationId)
    // .orElseThrow(() -> new IllegalStateException("Notification ID{" +
    // notificationId + "} Doesn't Exist"));
    // notificationRepository.delete(notification);
    // }

    @Transactional
    public void clearNotifications(Long userId) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        mailbox.getNotifications().clear();
    }

}
