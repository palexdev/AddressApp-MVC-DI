package io.github.palexdev.addressapp.events.base;

@FunctionalInterface
public interface NotificationObserver<T> {
    void receivedNotification(T messageType, Object... payload);
}
