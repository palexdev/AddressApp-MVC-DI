package io.github.palexdev.addressapp;

import io.github.palexdev.addressapp.events.SpringAppEvents;
import io.github.palexdev.addressapp.events.base.IEnumNotificationCentre;
import io.github.palexdev.addressapp.view.MainView;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;

public class SpringApp extends Application {
    private final StringProperty appTitle = new SimpleStringProperty("AddressApp");

    @Inject
    private SpringHelper helper;

    @Inject
    private IEnumNotificationCentre<SpringAppEvents> notificationCentre;

    @Inject
    private MainView view;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SpringConfig.initContext(this);
        registerComponents(primaryStage);
        initNotifications();

        Parent mainView = view.getView();
        Scene scene = new Scene(mainView);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.titleProperty().bind(appTitle);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().addAll(
                new Image(ResourceManager.loadResource("icons/appicon16.png")),
                new Image(ResourceManager.loadResource("icons/appicon32.png")),
                new Image(ResourceManager.loadResource("icons/appicon64.png"))
        );

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void registerComponents(Stage primaryStage) {
        helper.context().registerBean("primaryStage", Stage.class, () -> primaryStage);
        helper.context().registerBean("hostServices", HostServices.class, this::getHostServices);
    }

    private void initNotifications() {
        notificationCentre.subscribe(SpringAppEvents.UPDATE_STAGE_TITLE, (messageType, payload) -> {
            String s = (String) payload[0];
            appTitle.set(s);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}