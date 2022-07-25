package pro.sky.telegrambot.repository;


import com.sun.nio.sctp.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("FROM Notification WHERE notificationDate <= CURRENT_TIMESTAMP AND status = 'SCHEDULED'")
    Collection<Notification> getScheduledNotification();

}
