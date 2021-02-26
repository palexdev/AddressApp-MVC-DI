package io.github.palexdev.addressapp;

import com.gluonhq.ignite.spring.SpringContext;
import io.github.palexdev.addressapp.view.MainView;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class SpringApp extends Application {
    private final StringProperty appTitle = new SimpleStringProperty("AddressApp");
    private final SpringContext context = new SpringContext(
            this,
            () -> List.of(
                    SpringApp.class.getPackage().getName()
            )
    );

    @Override
    public void start(Stage primaryStage) throws Exception {
        context.init();
        Parent mainView = new MainView().getView();
        Scene scene = new Scene(mainView);

        primaryStage.titleProperty().bind(appTitle);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
