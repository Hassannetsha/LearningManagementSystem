package project_software.main.Notification.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project_software.main.Notification.Entities.Mailbox;
import project_software.main.Notification.Entities.Notification;
import project_software.main.Notification.Repositories.MailboxRepository;
import project_software.main.Notification.Repositories.NotificationRepository;

@Service
public class MailboxService {
    private final MailboxRepository mailboxRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public MailboxService(MailboxRepository mailboxRepository, NotificationRepository notificationRepository) {
        this.mailboxRepository = mailboxRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        return mailbox.getNotifications();
    }

    public void postNotificationToMailbox(Long userId, String message) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        Notification notification = new Notification(mailbox, message);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllNotificationsAsRead(Long userId) {
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
    public void markAllNotificationsAsUnread(Long userId) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        mailbox.getNotifications().forEach(notification -> notification.setIsRead(false));
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalStateException("Notification ID{" + notificationId + "} Doesn't Exist"));
        notificationRepository.delete(notification);
    }

    @Transactional
    public void clearNotifications(Long userId) {
        Mailbox mailbox = mailboxRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mailbox not found for user ID " + userId));
        mailbox.getNotifications().clear();
    }

}
