package pro.sky.telegrambot.Model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Notification {

    public enum NotificationStatus{
        SCHEDULED,
        SENT,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    private long chatId;

    private String notificationMessage;

    private LocalDateTime notificationDate;

    private LocalDateTime sentDate;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.SCHEDULED;

    public Notification(){
    }

    public Notification(String notificationMessage, LocalDateTime notificationDate){
        this.notificationMessage = notificationMessage;
        this.notificationDate = notificationDate;
    }

    public Long getChatId(){
        return chatId;
    }

    public void setChatId(Long chatId){
        this.chatId = chatId;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage){
        this.notificationMessage = notificationMessage;
    }

    public LocalDateTime getNotificationDate(){
        return notificationDate;
    }

    public void setNotificationDate(LocalDateTime notificationDate){
        this.notificationDate = notificationDate;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status){
        this.status = status;
    }

    public void markAsSent(){
        this.status = NotificationStatus.SENT;
        this.sentDate = LocalDateTime.now();
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public LocalDateTime getSentDate(){
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate){
        this.sentDate = sentDate;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId)
                && Objects.equals(notificationMessage, that.notificationMessage)
                && Objects.equals(notificationDate, that.notificationDate)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, chatId, notificationMessage, notificationDate, status);
    }

    @Override
    public String toString(){
        return "Notification{"
                + "id=" + id
                + ", chatId=" + chatId
                + ", notificationMessage='" + notificationMessage
                + '\'' + ", notificationDate=" + notificationDate
                + ", status=" + status
                + '}';
    }
}
