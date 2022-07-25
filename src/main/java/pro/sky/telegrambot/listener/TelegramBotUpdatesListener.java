package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.sun.nio.sctp.Notification;
import org.apache.naming.factory.SendMailFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private static final String START_CMD = "/start";

    private static final String GREETING_TEXT = " Привет, друзья! Я - телеграм бот";

    private static final String WELCOME = " Я веду учет о твоих важных мероприятиях." +
            " Просто введи запрос в виде: ДД.ММ.ГГГГ ЧЧ:ММ, необходимое мероприятие и я постараюсь его не забыть)";



    @Autowired
    private final TelegramBot telegramBot;

    private final NotificationService notificationService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void notifyScheduledTasks() {
        notificationService.notifyScheduledTasks(this::sendMessage);
    }


    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            if (message.text().startsWith(START_CMD)) {
                logger.info(START_CMD + " Запрос получен");
                sendMessage(extractChatId(message), GREETING_TEXT);
            } else {
                notificationService.parse(message.text()).ifPresentOrElse {
                    task -> scheduleNotification(extractChatId(message), task),
                            () -> sendMessage(extractChatId(message), WELCOME);
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void scheduleNotification(Long chatId, Notification task) {
        notificationService.schedule(task, chatId);
        sendMessage(chatId, " Запрос зарегистрирован");
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage(chatId, messageText);
        telegramBot.execute(sendMessage);
    }

    private void sendMessage(Notification task) {
        sendMessage(task.getChatId(), task.getNotificationMessage());
    }

    private Long extractChatId(Message message) {
        return message.chat().id();
    }
}


