package org.example.lmsproject.Notification;

import org.example.lmsproject.Notification.Controllers.MailboxController;
import org.example.lmsproject.Notification.Entities.Mailbox;
import org.example.lmsproject.Notification.Entities.Notification;
import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.Notification.Services.EmailService;
import org.example.lmsproject.Notification.Repositories.MailboxRepository;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.model.User.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MailboxControllerTest {

//    @Mock
//    private MailboxRepository mailboxRepository;
//
//    @Mock
//    private MailboxService mailboxService;
//
//    @Mock
//    private EmailService emailService;
//    @InjectMocks
//    private MailboxController mailboxController;
//
//    private MockMvc mockMvc;
//
//    private User testUser;
//    private Mailbox testMailbox;
//    private Notification testNotification;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(mailboxController).build();
//
//        testUser = new User("Nour", "password123", "nouredinshimi@gmail.com", Role.ROLE_STUDENT);
//        testUser.setId(1L);
//
//        testNotification = new Notification();
//        testNotification.setId(1L);
//        testNotification.setMessage("Test Notification");
//        testNotification.setIsRead(false);
//        testNotification.setDate(LocalDate.now());
//
//        testMailbox = new Mailbox(testUser);
//        testMailbox.setId(1L);
//        testMailbox.setNotifications(Collections.singletonList(testNotification));
//    }
//
//    @Test
//    void testGetMailbox() throws Exception {
//        when(mailboxService.getMailbox(1L)).thenReturn(testMailbox);
//
//        mockMvc.perform(get("/user/mailbox/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.user.id").value(1L))
//                .andExpect(jsonPath("$.notifications[0].message").value("Test Notification"));
//
//        verify(mailboxService, times(1)).getMailbox(1L);
//    }
//
//    @Test
//    void testGetNotifications() throws Exception {
//        Mailbox mailbox = new Mailbox();
//        mailbox.setId(1L);
//
//        Notification notification1 = new Notification(mailbox, "Notification 1");
//        Notification notification2 = new Notification(mailbox, "Notification 2");
//        List<Notification> notifications = Arrays.asList(notification1, notification2);
//
//        when(mailboxService.getNotifications(1L)).thenReturn(notifications);
//
//        mockMvc.perform(get("/user/mailbox/getNotifications/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].message").value("Notification 1"))
//                .andExpect(jsonPath("$[1].message").value("Notification 2"));
//
//        verify(mailboxService, times(1)).getNotifications(1L);
//    }
//
//    @Test
//    void testAddNotification() throws Exception {
//        Mailbox mailbox = new Mailbox();
//        mailbox.setId(1L);
//        mailbox.setNotifications(new ArrayList<>());
//        mailbox.setUser(testUser);
//
//        when(mailboxRepository.findByUserId(1L)).thenReturn(Optional.of(mailbox));
//
//        doNothing().when(mailboxService).addNotification(anyLong(), anyString());
//
//        mockMvc.perform(post("/user/mailbox")
//                        .param("userId", "1")
//                        .param("message", "Test Message"))
//                .andExpect(status().isOk());
//
//        verify(mailboxService, times(1)).addNotification(eq(1L), eq("Test Message"));
//
//        assertTrue(mailbox.getNotifications().stream()
//                .anyMatch(notification -> notification.getMessage().equals("Test Message")));
//    }
//
//
//
//
//    @Test
//    void testAddBulkNotifications() throws Exception {
//        doNothing().when(mailboxService).addBulkNotifications(anyList(), eq("Bulk Message"));
//
//        mockMvc.perform(post("/user/mailbox/bulk")
//                        .param("userIDs", "1,2,3")
//                        .param("message", "Bulk Message"))
//                .andExpect(status().isOk());
//
//        verify(mailboxService, times(1)).addBulkNotifications(anyList(), eq("Bulk Message"));
//    }
//
//    @Test
//    void testMarkAllAsRead() throws Exception {
//        doNothing().when(mailboxService).markAllAsRead(1L);
//
//        mockMvc.perform(put("/user/mailbox/markAllAsRead/1"))
//                .andExpect(status().isOk());
//
//        verify(mailboxService, times(1)).markAllAsRead(1L);
//    }
//
//    @Test
//    void testMarkAllAsUnread() throws Exception {
//        doNothing().when(mailboxService).markAllAsUnread(1L);
//
//        mockMvc.perform(put("/user/mailbox/markAllAsUnread/1"))
//                .andExpect(status().isOk());
//
//        verify(mailboxService, times(1)).markAllAsUnread(1L);
//    }
//
//    @Test
//    void testClearNotifications() throws Exception {
//        doNothing().when(mailboxService).clearNotifications(1L);
//
//        mockMvc.perform(delete("/user/mailbox/clear/1"))
//                .andExpect(status().isOk());
//
//        verify(mailboxService, times(1)).clearNotifications(1L);
//    }
}
