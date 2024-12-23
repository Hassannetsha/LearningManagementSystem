package org.example.lmsproject.Notification;

import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.Notification.Entities.Mailbox;
import org.example.lmsproject.Notification.Entities.Notification;
import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.example.lmsproject.Notification.Repositories.MailboxRepository;
import org.example.lmsproject.Notification.Services.EmailService;
import org.example.lmsproject.Notification.Services.NotificationService;
import org.example.lmsproject.userPart.model.User;

import org.example.lmsproject.userPart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailboxServiceTest {

    @Mock
    private MailboxRepository mailboxRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MailboxService mailboxService;

    private User testUser;
    private Mailbox testMailbox;
    private NotificationAndEmailMapper notificationAndEmailMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User("Nour", "password123", "nouredinshimi@gmail.com", User.Role.ROLE_STUDENT);
        testUser.setId(1L);

        testMailbox = new Mailbox(testUser);
        testMailbox.setId(1L);

        notificationAndEmailMapper = mock(NotificationAndEmailMapper.class);
        when(notificationAndEmailMapper.getSubject()).thenReturn("Test Subject");
        when(notificationAndEmailMapper.getBody()).thenReturn("Test Body");
    }

    @Test
    void testGetMailbox() {
        when(mailboxRepository.findById(1L)).thenReturn(Optional.of(testMailbox));

        Mailbox mailbox = mailboxService.getMailbox(1L);
        assertNotNull(mailbox);
        assertEquals(1L, mailbox.getId());
        assertEquals(testUser.getId(), mailbox.getUser().getId());
    }

    @Test
    void testGetMailbox_NotFound() {
        when(mailboxRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> mailboxService.getMailbox(1L));
        assertEquals("Mailbox not found for ID 1", exception.getMessage());
    }

    @Test
    void testGetNotifications() {
        Notification notification1 = new Notification(testMailbox, notificationAndEmailMapper);
        Notification notification2 = new Notification(testMailbox, notificationAndEmailMapper);
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(mailboxRepository.findByUserId(1L)).thenReturn(Optional.of(testMailbox));

        when(mailboxRepository.findByUserId(1L)).thenReturn(Optional.of(testMailbox));
        testMailbox.setNotifications(notifications);

        List<Notification> result = mailboxService.getNotifications(1L);
        
        assertEquals(2, result.size());
        assertEquals("Test Body", result.get(0).getMessage());
    }


    @Test
    void testAddNotification() {
        when(mailboxRepository.findByUserId(1L)).thenReturn(Optional.of(testMailbox));
        doNothing().when(notificationService).createNotification(testMailbox, notificationAndEmailMapper);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        mailboxService.addNotification(1L, notificationAndEmailMapper);

        verify(notificationService, times(1)).createNotification(testMailbox, notificationAndEmailMapper);
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testAddNotification_NewMailbox() {
        when(mailboxRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userService.getUser(1L)).thenReturn(testUser);
        doNothing().when(notificationService).createNotification(any(Mailbox.class), any(NotificationAndEmailMapper.class));
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        mailboxService.addNotification(1L, notificationAndEmailMapper);

        verify(mailboxRepository, times(1)).save(any(Mailbox.class));
        verify(notificationService, times(1)).createNotification(any(Mailbox.class), any(NotificationAndEmailMapper.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testMarkAllAsRead() {

        User testUser2 = new User("Hassan", "123456", "HassanMuhamed@gmail.com", User.Role.ROLE_STUDENT);
        testUser2.setId(2L);

        Mailbox testMailbox2  = new Mailbox(testUser2);;
        testMailbox.setId(2L);

        Notification notification1 = new Notification(testMailbox, notificationAndEmailMapper);
        notification1.setIsRead(false);

        Notification notification2 = new Notification(testMailbox, notificationAndEmailMapper);
        notification2.setIsRead(false);

        Notification notification3 = new Notification(testMailbox2, notificationAndEmailMapper);
        notification3.setIsRead(false);

        testMailbox.setNotifications(Arrays.asList(notification1, notification2));
        testMailbox2.setNotifications(Arrays.asList(notification3));

        when(mailboxRepository.findByUserId(1L)).thenReturn(Optional.of(testMailbox));
        mailboxService.markAllAsRead(1L);

        assertTrue(notification1.getIsRead(), "Notification 1 should be marked as read.");
        assertTrue(notification2.getIsRead(), "Notification 2 should be marked as read.");
        assertFalse(notification3.getIsRead(), "Notification 3 should Still be Unread");
        verify(mailboxRepository, times(1)).findByUserId(1L);
    }


    @Test
    void testClearNotifications() {
        Notification notification1 = new Notification(testMailbox, notificationAndEmailMapper);
        Notification notification2 = new Notification(testMailbox, notificationAndEmailMapper);
        List<Notification> notifications = new ArrayList<>(Arrays.asList(notification1, notification2));
        testMailbox.setNotifications(notifications);

        assertFalse(testMailbox.getNotifications().isEmpty(), "Mailbox should initially contain notifications.");

        when(mailboxRepository.findByUserId(1L)).thenReturn(Optional.of(testMailbox));
        mailboxService.clearNotifications(1L);

        assertTrue(testMailbox.getNotifications().isEmpty(), "Mailbox should be empty after calling clearNotifications.");
    }

}
