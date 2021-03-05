package io.github.palexdev.addressapp.events;

import io.github.palexdev.addressapp.events.base.IStringNotificationCentre;
import io.github.palexdev.addressapp.events.base.NotificationObserver;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class StringNotificationCentre implements IStringNotificationCentre {
    private final Logger logger = LogManager.getLogger(StringNotificationCentre.class);
    private final ObserverMap stringObservers = new ObserverMap();

    @Override
    public void subscribe(String messageType, NotificationObserver<String> observer) {
        Objects.requireNonNull(observer);
        addObserver(messageType, observer);
    }

    @Override
    public void unsubscribe(String messageType, NotificationObserver<String> observer) {
        removeObserversForMessageType(messageType, observer);
    }

    @Override
    public void unsubscribe(NotificationObserver<String> observer) {
        removeObserverFromObserverMap(observer);
    }

    @Override
    public void publish(String messageType, Object... payload) {
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
        stringObservers.clear();
    }

    // Utils

    private void publish(boolean isFxThread, String messageType, Object... payload) {
        logger.info("Publishing on FX Thread?: " + isFxThread);

        Collection<NotificationObserver<String>> notificationReceivers = stringObservers.get(messageType);
        if (notificationReceivers != null) {

            // make a copy to prevent ConcurrentModificationException if inside of an observer a new observer is subscribed.

            for (NotificationObserver<String> observer : notificationReceivers) {
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

    private void addObserver(String messageType, NotificationObserver<String> observer) {
        if (!stringObservers.containsKey(messageType)) {
            // use CopyOnWriteArrayList to prevent ConcurrentModificationException if inside of an observer a new observer is subscribed.
            stringObservers.put(messageType, new CopyOnWriteArrayList<>());
        }

        final List<NotificationObserver<String>> observers = stringObservers.get(messageType);

        if (observers.contains(observer)) {
            logger.warn("Subscribe the observer [" + observer + "] for the message [" + messageType +
                    "], but the same observer was already added for this message in the past.");
        }
        observers.add(observer);
    }

    private void removeObserverFromObserverMap(NotificationObserver<String> observer) {
        for (String key : stringObservers.keySet()) {
            final List<NotificationObserver<String>> observers = stringObservers.get(key);

            removeObserverFromObserverList(observer, observers);

            if (observers.isEmpty()) {
                stringObservers.remove(key);
            }
        }
    }

    private void removeObserverFromObserverList(NotificationObserver<String> observer, List<NotificationObserver<String>> observerList) {
        observerList.removeIf(actualObserver -> actualObserver.equals(observer));

        observerList.removeIf(actualObserver -> {
            if (actualObserver instanceof WeakNotificationObserver) {
                WeakNotificationObserver<String> weakObserver = (WeakNotificationObserver<String>) actualObserver;

                NotificationObserver<String> wrappedObserver = weakObserver.getWrappedObserver();

                if (wrappedObserver == null) { // if reference was GCed we can remove the weakObserver
                    return true;
                } else {
                    return wrappedObserver.equals(observer);
                }
            }

            return false;
        });
    }

    private void removeObserversForMessageType(String messageName, NotificationObserver<String> observer) {

        if (stringObservers.containsKey(messageName)) {
            final List<NotificationObserver<String>> observers = stringObservers.get(messageName);
            removeObserverFromObserverList(observer, observers);

            if (observers.isEmpty()) {
                stringObservers.remove(messageName);
            }
        }
    }

    private static class ObserverMap extends HashMap<String, List<NotificationObserver<String>>> {}
}
