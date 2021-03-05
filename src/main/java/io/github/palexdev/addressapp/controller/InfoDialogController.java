package io.github.palexdev.addressapp.controller;

import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoDialogController implements Initializable {

    @FXML
    private MFXFontIcon closeIcon;

    @FXML
    private Label githubL;

    @FXML
    private Hyperlink githubH;

    @FXML
    private Label contactL;

    @FXML
    private Hyperlink contactH;

    @FXML
    private Label emailL;

    @FXML
    private Hyperlink emailH;

    @FXML
    private Label tutorialL;

    @FXML
    private Hyperlink tutorialH;

    @Inject
    private HostServices hostServices;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        githubL.getGraphic().setOnMousePressed(event -> hostServices.showDocument(githubH.getTooltip().getText()));
        contactL.getGraphic().setOnMousePressed(event -> hostServices.showDocument(contactH.getTooltip().getText()));
        emailL.getGraphic().setOnMousePressed(event -> hostServices.showDocument(emailH.getTooltip().getText()));
        tutorialL.getGraphic().setOnMousePressed(event -> hostServices.showDocument(tutorialH.getTooltip().getText()));

        closeIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> ((Stage) closeIcon.getScene().getWindow()).close());
    }
}
