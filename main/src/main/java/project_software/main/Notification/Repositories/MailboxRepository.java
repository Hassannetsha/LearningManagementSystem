package project_software.main.Notification.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.main.Notification.Entities.Mailbox;

@Repository
public interface MailboxRepository extends JpaRepository<Mailbox, Long> {
    Optional<Mailbox> findByUserId(Long userId);
}

