package org.example.lmsproject.Notification.Services;

import java.util.List;

import org.example.lmsproject.Notification.Entities.Mailbox;
import org.example.lmsproject.Notification.Entities.Notification;
import org.example.lmsproject.Notification.Repositories.MailboxRepository;
import org.example.lmsproject.Notification.TextMappers.EmailMapper;
import org.example.lmsproject.Notification.TextMappers.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailboxService {
    private final MailboxRepository mailboxRepository;
    // private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    @Autowired
    public MailboxService(MailboxRepository mailboxRepository,
                          NotificationService notificationService,
                          EmailService emailService) {
        this.mailboxRepository = mailboxRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
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

    public void addNotification(Long userId, Object message) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        notificationService.createNotification(mailbox, message);

        String email = mailbox.getUser().getEmail();
        String subject = EmailMapper.getSubject(message);
        String body = EmailMapper.getBody(message);
        emailService.sendEmail(email, subject, body);
    }

    public void addBulkNotifications(List<Long> userIds, Object message) {
        userIds.forEach(userId -> {
            Mailbox mailbox = mailboxRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
            notificationService.createNotification(mailbox, message);

            String email = mailbox.getUser().getEmail();
            String subject = EmailMapper.getSubject(message);
            String body = EmailMapper.getBody(message);
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
