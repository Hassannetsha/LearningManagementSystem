package org.example.lmsproject.Notification.Entities;

import java.time.LocalDate;

import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table
public class Notification {

    @Id
    @SequenceGenerator(
        name = "notification_sequence",
        sequenceName = "notification_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "notification_sequence"
    )

    private Long id;
    private Boolean isRead;
    private String message;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "mailbox_id", nullable = false)
    @JsonIgnore
    private Mailbox mailbox;

    public Notification() {}

    public Notification(Mailbox mailbox, NotificationAndEmailMapper notificationAndEmailMapper) {
        this.isRead = false;
        this.mailbox = mailbox;
        this.message = notificationAndEmailMapper.getBody();
        this.date = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Mailbox getMailbox() {
        return mailbox;
    }

    public void setMailbox(Mailbox mailbox) {
        this.mailbox = mailbox;
    }

    @Override
    public String toString() {
        return "Notification [id=" + id + ", isRead=" + isRead + ", message=" + message + ", date=" + date + ", mailbox="
                + mailbox.getId() + "]";
    }
}
