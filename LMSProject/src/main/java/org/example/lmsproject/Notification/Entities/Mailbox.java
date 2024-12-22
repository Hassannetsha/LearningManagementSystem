package org.example.lmsproject.Notification.Entities;

import java.util.List;
import jakarta.persistence.*;
import org.example.lmsproject.userPart.model.User;

@Entity
@Table(name = "mailbox")
public class Mailbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @OneToOne
     @JoinColumn(name = "user_id", nullable = false, unique = true)
     private User user;

    @OneToMany(mappedBy = "mailbox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    public Mailbox() {}

     public Mailbox(User user) {
         this.user = user;
     }

    public Long getId() {
        return id;
    }

     public User getUser() {
         return user;
     }

     public void setUser(User user) {
         this.user = user;
     }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

     @Override
     public String toString() {
         return "Mailbox [id=" + id + ", user=" + user.getId() + ", notifications=" + notifications + "]";
     }
}
