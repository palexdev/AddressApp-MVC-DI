package io.github.palexdev.addressapp.events;

import io.github.palexdev.addressapp.events.base.NotificationObserver;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

public final class WeakNotificationObserver<T> implements NotificationObserver<T> {

    private final Reference<NotificationObserver<T>> reference;

    public WeakNotificationObserver(NotificationObserver<T> notificationObserver) {
        reference = new WeakReference<>(Objects.requireNonNull(notificationObserver));
    }

    @Override
    public void receivedNotification(T key, Object... payload) {
        NotificationObserver<T> observer = reference.get();
        if (observer != null) {
            observer.receivedNotification(key, payload);
        }
    }

    /**
     * @return the reference of the wrapped {@link NotificationObserver}.
     * If the wrapped observer was already garbage collected, this returns <code>null</code>
     */
    NotificationObserver<T> getWrappedObserver() {
        return reference.get();
    }

}
