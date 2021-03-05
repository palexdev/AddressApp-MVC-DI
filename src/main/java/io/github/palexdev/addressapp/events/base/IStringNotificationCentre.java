package io.github.palexdev.addressapp.events.base;

public interface IStringNotificationCentre {
    void subscribe(String messageType, NotificationObserver<String> observer);
    void unsubscribe(String messageType, NotificationObserver<String> observer);
    void unsubscribe(NotificationObserver<String> observer);
    void publish(String messageType, Object... payload);
    void clear();
}
