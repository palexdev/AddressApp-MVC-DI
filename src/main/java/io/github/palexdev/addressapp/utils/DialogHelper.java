package io.github.palexdev.addressapp.utils;

import io.github.palexdev.addressapp.view.components.ExceptionDialog;
import io.github.palexdev.materialfx.controls.MFXDialog;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXStageDialog;
import io.github.palexdev.materialfx.controls.base.AbstractMFXDialog;
import io.github.palexdev.materialfx.controls.enums.DialogType;
import io.github.palexdev.materialfx.controls.factories.MFXDialogFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogHelper {

    private DialogHelper() {
    }

    public static MFXDialog getDialog(DialogType type, String title, String message) {
       return MFXDialogFactory.buildDialog(type, title, message);
    }

    public static MFXStageDialog getStageDialog(Stage stage, DialogType type, String title, String message) {
        MFXStageDialog stageDialog = new MFXStageDialog(type, title, message);
        stageDialog.setOwner(stage);
        stageDialog.setModality(Modality.APPLICATION_MODAL);
        return stageDialog;
    }

    public static MFXStageDialog getExceptionDialog(Stage stage, String title, Throwable th) {
        ExceptionDialog dialog = new ExceptionDialog();
        dialog.setTitle(title);
        dialog.setException(th);
        MFXStageDialog stageDialog = new MFXStageDialog(dialog);
        stageDialog.setOwner(stage);
        stageDialog.setModality(Modality.APPLICATION_MODAL);
        return stageDialog;
    }

    public static MFXStageDialog getLoadingDialog(Stage stage, String title, String message) {
        MFXStageDialog stageDialog = DialogHelper.getStageDialog(stage, DialogType.INFO, title, message);
        stageDialog.setOwner(stage);
        stageDialog.setModality(Modality.APPLICATION_MODAL);

        AbstractMFXDialog dialog = stageDialog.getDialog();
        dialog.setPrefHeight(300);
        StackPane center = (StackPane) dialog.getCenter();
        MFXProgressSpinner progressSpinner = new MFXProgressSpinner();
        progressSpinner.setRadius(30);
        center.getChildren().add(progressSpinner);

        return stageDialog;
    }
}
