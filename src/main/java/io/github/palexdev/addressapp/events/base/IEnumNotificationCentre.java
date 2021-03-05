package io.github.palexdev.addressapp.events.base;

public interface IEnumNotificationCentre<T extends Enum<T>> {
    void subscribe(T messageType, NotificationObserver<T> observer);
    void unsubscribe(T messageType, NotificationObserver<T> observer);
    void unsubscribe(NotificationObserver<T> observer);
    void publish(T messageType, Object... payload);
    void clear();
}
