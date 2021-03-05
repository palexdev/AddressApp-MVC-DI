package io.github.palexdev.addressapp.view.components;

import io.github.palexdev.addressapp.utils.ExceptionUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDialog;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.enums.ButtonType;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ExceptionDialog extends MFXDialog {
    private final TextArea exceptionArea;

    public ExceptionDialog() {
        setPrefSize(500, 350);
        setPadding(new Insets(3));

        StackPane headerNode = new StackPane();
        headerNode.setPrefSize(getPrefWidth(), getPrefHeight() * 0.45);
        headerNode.getStyleClass().add("header-node");
        headerNode.setStyle("-fx-background-color: #ff9e9e;\n" + "-fx-background-insets: -3 -3 0 -3");

        MFXFontIcon exceptionIcon = new MFXFontIcon("mfx-x-circle-light");
        exceptionIcon.setColor(Color.WHITE);
        exceptionIcon.setSize(96);
        MFXFontIcon closeIcon = new MFXFontIcon("mfx-x", Color.WHITE);

        MFXButton closeButton = new MFXButton("");
        closeButton.setPrefSize(20, 20);
        closeButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        closeButton.setGraphic(closeIcon);
        closeButton.setRippleRadius(15);
        closeButton.setRippleColor(Color.rgb(255, 0, 0, 0.1));
        closeButton.setRippleInDuration(Duration.millis(500));
        closeButton.setButtonType(ButtonType.FLAT);

        NodeUtils.makeRegionCircular(closeButton);

        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(7, 7, 0, 0));
        headerNode.getChildren().addAll(exceptionIcon, closeButton);

        exceptionArea = new TextArea();
        exceptionArea.setEditable(false);
        exceptionArea.setWrapText(true);
        exceptionArea.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

        StackPane scrollContent = new StackPane();
        scrollContent.setPadding(new Insets(-5));
        scrollContent.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        scrollContent.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        scrollContent.setPrefSize(590, 240);
        scrollContent.getChildren().setAll(exceptionArea);
        MFXScrollPane scrollPane = new MFXScrollPane(scrollContent);
        scrollPane.setPadding(new Insets(10, 10, 0, 10));
        scrollPane.setFitToWidth(true);

        setTop(headerNode);
        setCenter(scrollPane);
        setCloseButtons(closeButton);
    }

    public void setException(Throwable th) {
        exceptionArea.setText(ExceptionUtils.getStackTraceString(th));
    }
}
