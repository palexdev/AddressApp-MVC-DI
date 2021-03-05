package io.github.palexdev.addressapp.events;

import io.github.palexdev.addressapp.events.base.IEnumNotificationCentre;
import io.github.palexdev.addressapp.events.base.NotificationObserver;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnumNotificationCentre<T extends Enum<T>> implements IEnumNotificationCentre<T> {
    private final Logger logger = LogManager.getLogger(EnumNotificationCentre.class);
    private final ObserverMap enumObservers = new ObserverMap();

    @Override
    public void subscribe(T messageType, NotificationObserver<T> observer) {
        Objects.requireNonNull(observer);
        addObserver(messageType, observer);
    }

    @Override
    public void unsubscribe(T messageType, NotificationObserver<T> observer) {
        removeObserversForMessageType(messageType, observer);
    }

    @Override
    public void unsubscribe(NotificationObserver<T> observer) {
        removeObserverFromObserverMap(observer);
    }

    @Override
    public void publish(T messageType, Object... payload) {
        boolean isFxThread = shouldPublishInThisThread();
        if (isFxThread) {
            publish(true, messageType, payload);
        } else {
            try {
                Platform.runLater(() -> publish(false, messageType, payload));
            } catch (IllegalStateException ex) {

                // If the toolkit isn't initialized yet we will publish the notification directly.
                // In most cases this means that we are in a unit test and not JavaFX application is running.
                if (ex.getMessage().equals("Toolkit not initialized")) {
                    publish(false, messageType, payload);
                } else {
                    throw ex;
                }
            }
        }
    }

    @Override
    public void clear() {
        enumObservers.clear();
    }

    // Utils

    private void publish(boolean isFxThread, T messageType, Object... payload) {
        logger.info("Publishing on FX Thread?: " + isFxThread);

        Collection<NotificationObserver<T>> notificationReceivers = enumObservers.get(messageType);
        if (notificationReceivers != null) {

            // make a copy to prevent ConcurrentModificationException if inside of an observer a new observer is subscribed.

            for (NotificationObserver<T> observer : notificationReceivers) {
                observer.receivedNotification(messageType, payload);
            }
        }
    }

    private boolean shouldPublishInThisThread() {
        try {
            return Platform.isFxApplicationThread();
        } catch (final RuntimeException e) {
            if (e.getMessage().equals("No toolkit found")) {
                // If the toolkit is not even available, we publish the notification directly.
                // In most cases this means that we are in an environment where no JavaFX
                // application is running (probably also in a JUnit test).
                return true;
            } else {
                throw e;
            }
        }
    }

    private void addObserver(T messageType, NotificationObserver<T> observer) {
        if (!enumObservers.containsKey(messageType)) {
            // use CopyOnWriteArrayList to prevent ConcurrentModificationException if inside of an observer a new observer is subscribed.
            enumObservers.put(messageType, new CopyOnWriteArrayList<>());
        }

        final List<NotificationObserver<T>> observers = enumObservers.get(messageType);

        if (observers.contains(observer)) {
            logger.warn("Subscribe the observer [" + observer + "] for the message [" + messageType +
                    "], but the same observer was already added for this message in the past.");
        }
        observers.add(observer);
    }

    private void removeObserverFromObserverMap(NotificationObserver<T> observer) {
        for (T key : enumObservers.keySet()) {
            final List<NotificationObserver<T>> observers = enumObservers.get(key);

            removeObserverFromObserverList(observer, observers);

            if (observers.isEmpty()) {
                enumObservers.remove(key);
            }
        }
    }

    private void removeObserverFromObserverList(NotificationObserver<T> observer, List<NotificationObserver<T>> observerList) {
        observerList.removeIf(actualObserver -> actualObserver.equals(observer));

        observerList.removeIf(actualObserver -> {
            if (actualObserver instanceof WeakNotificationObserver) {
                WeakNotificationObserver<T> weakObserver = (WeakNotificationObserver<T>) actualObserver;

                NotificationObserver<T> wrappedObserver = weakObserver.getWrappedObserver();

                if (wrappedObserver == null) { // if reference was GCed we can remove the weakObserver
                    return true;
                } else {
                    return wrappedObserver.equals(observer);
                }
            }

            return false;
        });
    }

    private void removeObserversForMessageType(T messageName, NotificationObserver<T> observer) {

        if (enumObservers.containsKey(messageName)) {
            final List<NotificationObserver<T>> observers = enumObservers.get(messageName);
            removeObserverFromObserverList(observer, observers);

            if (observers.isEmpty()) {
                enumObservers.remove(messageName);
            }
        }
    }

    private class ObserverMap extends HashMap<T, List<NotificationObserver<T>>> {}
}
