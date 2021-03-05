package io.github.palexdev.addressapp.view;

import io.github.palexdev.addressapp.ResourceManager;
import io.github.palexdev.addressapp.view.base.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class PersonOverview implements View {
    private final String fxml = "fxml/PersonOverview.fxml";

    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public Parent getView() throws IOException {
        fxmlLoader.setLocation(ResourceManager.getResource(fxml));
        return fxmlLoader.load();
    }
}
