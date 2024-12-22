package org.example.lmsproject.Notification.Repositories;

import java.util.Optional;

import org.example.lmsproject.Notification.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Optional<Notification> findByMailbox_Id(long mailboxId);

    List<Notification> findAllByMailbox_Id(long mailboxId);

}