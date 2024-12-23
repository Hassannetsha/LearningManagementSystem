package org.example.lmsproject.Notification;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.lmsproject.Notification.Controllers.MailboxController;
import org.example.lmsproject.Notification.Entities.Mailbox;
import org.example.lmsproject.Notification.Entities.Notification;
import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

class MailboxControllerTest {

    @Mock
    private MailboxService mailboxservice;

    @InjectMocks
    private MailboxController mailboxcontroller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_getmailbox() {
        Long mailboxid = 1L;
        Mailbox mailbox = new Mailbox();
        when(mailboxservice.getMailbox(mailboxid)).thenReturn(mailbox);

        Mailbox result = mailboxcontroller.getMailbox(mailboxid);
        assertNotNull(result);
        assertSame(mailbox, result);
        verify(mailboxservice, times(1)).getMailbox(mailboxid);
    }

    @Test
    void test_getnotifications() {
        Long userid = 1L;
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(mailboxservice.getNotifications(userid)).thenReturn(notifications);

        List<Notification> result = mailboxcontroller.getNotifications(userid);

        assertEquals(2, result.size());
        verify(mailboxservice, times(1)).getNotifications(userid);
    }

    @Test
    void testAddNotification() {
        Long userid = 1L;
        NotificationAndEmailMapper message = mock(NotificationAndEmailMapper.class);
        when(message.getSubject()).thenReturn("test");
        when(message.getBody()).thenReturn("testB");

        mailboxcontroller.addNotification(userid, message);

        verify(mailboxservice, times(1)).addNotification(userid, message);
    }


    @Test
    void testMarkAllAsRead() {
        Long userid = 1L;
        mailboxcontroller.markAllAsRead(userid);
        verify(mailboxservice, times(1)).markAllAsRead(userid);
    }

    @Test
    void testMarkAllAsUnread() {
        Long userid = 1L;
        mailboxcontroller.markAllAsUnread(userid);
        verify(mailboxservice, times(1)).markAllAsUnread(userid);
    }

    @Test
    void testClearNotifications() {
        Long userid = 1L;
        mailboxcontroller.clearNotifications(userid);
        verify(mailboxservice, times(1)).clearNotifications(userid);
    }

}
