package project_software.main.Notification.MessageMapper;
import project_software.main.Notification.Entities.Mailbox;
import project_software.main.Notification.Entities.Notification;

public class MessageMapper {
    public static String toString(Object message) {
        if (message instanceof String) {
            String s = (String) message;
            return s;
        }
        return "";
    }

}

