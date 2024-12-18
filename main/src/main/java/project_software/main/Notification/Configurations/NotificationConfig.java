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
            // Notification one = new Notification("param", LocalDate.of(2024, 12, 17),1);
            // Notification two = new Notification(false, "2", LocalDate.of(2024, 12, 7),2);
            // Notification three = new Notification(false, "3", LocalDate.of(2024, 12, 1),3);
            // Notification unexpected = new Notification(false, "the spanish inquisition", LocalDate.of(1478, 11, 1),9);

            // repository.saveAll(List.of(one,two,three,unexpected));
        };
    }
}
