package org.example.lmsproject.Notification.Repositories;

import java.util.Optional;

import org.example.lmsproject.Notification.Entities.Mailbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MailboxRepository extends JpaRepository<Mailbox, Long> {
    Optional<Mailbox> findByUserId(Long userId);
}

