package project_software.main.Notification.Configurations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import project_software.main.Notification.Entities.Notification;
import project_software.main.Notification.Repositories.NotificationRepository;

@Configuration
public class NotificationConfig {

    @Bean
    CommandLineRunner commandLineRunner(NotificationRepository repository) {
        return args -> {
        };
    }
}
