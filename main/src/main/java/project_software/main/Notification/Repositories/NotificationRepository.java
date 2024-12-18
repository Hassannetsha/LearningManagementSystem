package project_software.main.Notification.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project_software.main.Notification.Entities.Notification;
import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>{

    Optional<Notification>  findByUserID(Long userID);
    List<Notification> findAllByUserID(long userID);
}
