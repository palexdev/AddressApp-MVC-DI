package io.github.palexdev.addressapp.view.components;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDialog;
import io.github.palexdev.materialfx.controls.enums.ButtonType;
import io.github.palexdev.materialfx.effects.DepthLevel;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ExitDialog extends MFXDialog {
    private final MFXButton saveButton;
    private final MFXButton saveExitButton;
    private final MFXButton cancelButton;
    private final MFXButton exitButton;

    public ExitDialog() {
        setTitle("Unsaved Changes!!");
        setContent("There are unsaved changes, what do you want to do?");

        setPrefSize(550, 300);
        setPadding(new Insets(3));

        MFXFontIcon warningIcon = new MFXFontIcon("mfx-exclamation-triangle");
        warningIcon.setColor(Color.WHITE);
        warningIcon.setSize(96);

        StackPane headerNode = new StackPane(warningIcon);
        headerNode.setPrefSize(getPrefWidth(), getPrefHeight() * 0.45);
        headerNode.getStyleClass().add("header-node");
        headerNode.setStyle("-fx-background-color: #ffa57f;\n" + "-fx-background-insets: -3 -3 0 -3");

        Label titleLabel = new Label();
        titleLabel.textProperty().bind(titleProperty());
        StackPane.setMargin(titleLabel, new Insets(10, 0, 10, 0));
        Label contentLabel = new Label();
        contentLabel.textProperty().bind(contentProperty());
        StackPane.setMargin(contentLabel, new Insets(40, 0, 40, 0));

        saveButton = new MFXButton("Save");
        saveExitButton = new MFXButton("Save and Exit");
        cancelButton = new MFXButton("Cancel");
        exitButton = new MFXButton("Exit");

        saveButton.setPrefWidth(100);
        saveExitButton.setPrefWidth(100);
        cancelButton.setPrefWidth(100);
        exitButton.setPrefWidth(100);

        saveButton.setButtonType(ButtonType.RAISED);
        saveExitButton.setButtonType(ButtonType.RAISED);
        cancelButton.setButtonType(ButtonType.RAISED);
        exitButton.setButtonType(ButtonType.RAISED);

        saveButton.setDepthLevel(DepthLevel.LEVEL1);
        saveExitButton.setDepthLevel(DepthLevel.LEVEL1);
        cancelButton.setDepthLevel(DepthLevel.LEVEL1);
        exitButton.setDepthLevel(DepthLevel.LEVEL1);

        HBox buttonsContainer = new HBox(20, saveButton, saveExitButton, cancelButton, exitButton);
        buttonsContainer.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        StackPane.setAlignment(buttonsContainer, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(buttonsContainer, new Insets(0, 10, 10, 0));

        StackPane dialogContent = new StackPane(titleLabel, contentLabel, buttonsContainer);
        dialogContent.setAlignment(Pos.TOP_CENTER);
        dialogContent.setPadding(new Insets(-5));
        dialogContent.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        dialogContent.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        dialogContent.setPrefSize(500, 180);

        setTop(headerNode);
        setCenter(dialogContent);
        setCloseButtons(cancelButton);

        installTooltips();
    }

    private void installTooltips() {
        Tooltip.install(saveButton, new Tooltip("Saves and closes this dialog"));
        Tooltip.install(saveExitButton, new Tooltip("Saves and closes the app"));
        Tooltip.install(cancelButton, new Tooltip("Closes this dialog"));
        Tooltip.install(exitButton, new Tooltip("Closes the app, any unsaved changes will be lost!!"));
    }

    public void setSaveAction(EventHandler<ActionEvent> action) {
        saveButton.setOnAction(action);
    }

    public void setSaveExitAction(EventHandler<ActionEvent> action) {
        saveExitButton.setOnAction(action);
    }

    public void setExitAction(EventHandler<ActionEvent> action) {
        exitButton.setOnAction(action);
    }
}
