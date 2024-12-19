package project_software.main.Notification.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project_software.main.Notification.Entities.Mailbox;
import project_software.main.Notification.Entities.Notification;
import project_software.main.Notification.Repositories.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // public List<Notification> getNotifications() {
    // return notificationRepository.findAll();
    // }

    // public List<Notification> getNotificationsByUserID(long id) {
    // return notificationRepository.findAllByUserID(id);
    // }

    // public void postNotification(Notification notification) {
    // notificationRepository.save(notification);
    // }

    public Notification createNotification(Mailbox mailbox, Object message) {
        Notification notification = new Notification(mailbox, message);
        return notificationRepository.save(notification);
    }

    // public void postBulkNotifications(List<Long> userIDs, String message) {
    // List<Notification> notifications = userIDs.stream()
    // .map(userId -> new Notification(userId, message))
    // .collect(Collectors.toList());
    // notificationRepository.saveAll(notifications);
    // }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalStateException("Notification not found for ID " + notificationId));
        notificationRepository.delete(notification);
    }

    // @Transactional
    // public void markAsRead(long id) {
    // Notification notification = notificationRepository.findById(id)
    // .orElseThrow(() -> new IllegalStateException("Notification ID{" + id + "}
    // Doesn't Exist"));
    // notification.setIsRead(true);
    // }

    // @Transactional
    // public void markAllAsRead(long id) {
    // List<Notification> notifications =
    // notificationRepository.findAllByUserID(id);
    // for (Notification notification : notifications) {
    // notification.setIsRead(true);
    // }
    // }

    // @Transactional
    // public void markAsUnread(long id) {
    // Notification notification = notificationRepository.findById(id)
    // .orElseThrow(() -> new IllegalStateException("Notification ID{" + id + "}
    // Doesn't Exist"));
    // notification.setIsRead(false);
    // }
}
