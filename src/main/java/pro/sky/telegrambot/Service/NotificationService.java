package pro.sky.telegrambot.Service;

import pro.sky.telegrambot.Model.Notification;

import java.util.Optional;
import java.util.function.Consumer;

public interface NotificationService {

    Notification schedule(Notification notification, Long chatId);

    Optional<Notification> parse(String notificationBotMessage);

    void notifyAllScheduledTasks(Consumer<Notification> notifier);

}
