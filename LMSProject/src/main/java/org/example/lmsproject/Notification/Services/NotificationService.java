package org.example.lmsproject.Notification.Services;

import org.example.lmsproject.Notification.Entities.Mailbox;
import org.example.lmsproject.Notification.Entities.Notification;
import org.example.lmsproject.Notification.Repositories.NotificationRepository;
import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public void createNotification(Mailbox mailbox, NotificationAndEmailMapper notificationAndEmailMapper) {
        Notification notification = new Notification(mailbox, notificationAndEmailMapper);
        notificationRepository.save(notification);
//        return notificationRepository.save(notification);
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
