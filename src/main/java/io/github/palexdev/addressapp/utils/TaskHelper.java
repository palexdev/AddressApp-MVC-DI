package io.github.palexdev.addressapp.utils;

import javafx.concurrent.Task;

public class TaskHelper {

    private TaskHelper() {}

    public static void runTask(Task<?> task) {
        runTask(task, "");
    }

    public static void runTask(Task<?> task, String name) {
        Thread thread = new Thread(task, name);
        thread.setDaemon(true);
        thread.start();
    }
}
