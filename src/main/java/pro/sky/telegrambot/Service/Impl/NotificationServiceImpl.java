package pro.sky.telegrambot.Service.Impl;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Model.Notification;
import pro.sky.telegrambot.Service.NotificationService;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationServiceImpl implements NotificationService {


    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private static final String REGEX_BOT_MESSAGE = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }


    @Override
    public Notification schedule(Notification task, Long chatId) {
        task.setChatId(chatId);

        Notification storedTask = repository.save(task);
        logger.info(" Запрос был успешно сохранен" + storedTask);
        return storedTask;
    }

    @Override
    public Optional<Notification> parse(String notificationBotMessage) {
        Pattern pattern = Pattern.compile(REGEX_BOT_MESSAGE);
        Matcher matcher = pattern.matcher(notificationBotMessage);

        Notification result = null;
        try {
            if (matcher.find()) {
                LocalDateTime notificationDateTime = LocalDateTime.parse(matcher.group(1)), DATE_TIME_FORMATTER;
                String notification = matcher.group(3);
                result = new Notification(notification, notificationDateTime);
            }
        } catch (DateTimeParseException e) {
            logger.error(" Failed to parse localDateTime" + notificationBotMessage + " with pattern" + DATE_TIME_FORMATTER);
        } catch (RuntimeException e) {
            logger.error(" Failed to parse notificationBotMessage" + notificationBotMessage, e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public void notifyAllScheduledTasks(Consumer<Notification> notifier) {
        logger.info(" Trigger sending of scheduled notifications");
        Collection<com.sun.nio.sctp.Notification> notifications = repository.getScheduledNotification();
        logger.info(" Found {} notification, processing...", notifications.size());
        for (Notification task : notifications) {
            notifier.accept(task);
            task.markAsSent();
        }
        repository.saveAll(notifications);
        logger.info(" Finish to processing scheduled notifications");
    }
}
