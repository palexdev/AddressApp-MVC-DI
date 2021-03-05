package io.github.palexdev.addressapp.controller;

import io.github.palexdev.addressapp.ResourceManager;
import io.github.palexdev.addressapp.dao.base.DAOInterface;
import io.github.palexdev.addressapp.events.EnumNotificationCentre;
import io.github.palexdev.addressapp.events.SpringAppEvents;
import io.github.palexdev.addressapp.model.PeopleRepository;
import io.github.palexdev.addressapp.utils.DAOUtils;
import io.github.palexdev.addressapp.utils.DialogHelper;
import io.github.palexdev.addressapp.utils.TaskHelper;
import io.github.palexdev.addressapp.view.InfoDialogView;
import io.github.palexdev.addressapp.view.PersonOverview;
import io.github.palexdev.addressapp.view.components.ChartDialog;
import io.github.palexdev.addressapp.view.components.ExitDialog;
import io.github.palexdev.materialfx.controls.MFXDialog;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXStageDialog;
import io.github.palexdev.materialfx.controls.factories.MFXAnimationFactory;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.NodeUtils;
import io.github.palexdev.materialfx.utils.StringUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.jfr.Name;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @Inject
    @Name("primaryStage")
    private Stage primaryStage;

    @Inject
    private PersonOverview personOverview;

    @Inject
    private InfoDialogView infoDialogView;

    @Inject
    private PeopleRepository repo;

    @Inject
    private DAOInterface dao;

    @Inject
    private EnumNotificationCentre<SpringAppEvents> notificationCentre;

    @FXML
    private MFXLabel title;

    @FXML
    private VBox box;

    @FXML
    private MenuItem addMenu;

    @FXML
    private MenuItem removeMenu;

    @FXML
    private MenuItem editMenu;

    @FXML
    private MFXIconWrapper minimizeIcon;

    @FXML
    private MFXIconWrapper closeIcon;

    @FXML
    private MFXLabel changesLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPersonOverview();
        TaskHelper.runTask(loadRepoTask(DAOUtils.readRepoPath()));
        setupWindowControls();

        title.textProperty().bind(primaryStage.titleProperty());
        title.setLeadingIcon(new ImageView(ResourceManager.getResource("icons/appicon16.png").toString()));

        changesLabel.setLeadingIcon(new MFXFontIcon("mfx-exclamation-triangle", 12, Color.ORANGERED));
        changesLabel.setTrailingIcon(new MFXFontIcon("mfx-exclamation-triangle", 12, Color.ORANGERED));
        notificationCentre.subscribe(SpringAppEvents.CHANGES_OCCURRED, (messageType, payload) -> {
            if (changesLabel.getOpacity() == 0) {
                MFXAnimationFactory.FADE_IN.build(changesLabel, 600).play();
            }
        });

        addMenu.setOnAction(event -> notificationCentre.publish(SpringAppEvents.MENU_ADD));
        removeMenu.setOnAction(event -> notificationCentre.publish(SpringAppEvents.MENU_REMOVE));
        editMenu.setOnAction(event -> notificationCentre.publish(SpringAppEvents.MENU_EDIT));
    }

    private void loadPersonOverview() {
        try {
            Parent parent = personOverview.getView();
            box.getChildren().add(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Task<Boolean> loadRepoTask(Path repoPath) {
        MFXStageDialog loadingDialog = DialogHelper.getLoadingDialog(primaryStage, "Loading repository...", "");

        Task<Boolean> loadRepoTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                Platform.runLater(loadingDialog::show);

                if (!repoPath.toString().trim().isEmpty()) {
                    dao.loadRepository(repoPath);
                }
                return true;
            }
        };

        loadRepoTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, event -> {
            loadingDialog.close();
            DialogHelper.getExceptionDialog(primaryStage, "Load Repo Task Failed", loadRepoTask.getException()).show();

        });
        loadRepoTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            notificationCentre.publish(SpringAppEvents.UPDATE_STAGE_TITLE, "AddressApp - " + StringUtils.replaceLast(repoPath.getFileName().toString(), ".json", ""));
            loadingDialog.close();
        });

        return loadRepoTask;
    }

    private Task<Boolean> saveRepoTask(Path repoPath) {
        MFXStageDialog savingDialog = DialogHelper.getLoadingDialog(primaryStage, "Saving repository...", "");

        Task<Boolean> saveRepoTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                Platform.runLater(savingDialog::show);

                if (!repoPath.toString().trim().isEmpty()) {
                    dao.saveToFile(repoPath);
                }
                return true;
            }
        };

        saveRepoTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, event -> {
            savingDialog.close();
            DialogHelper.getExceptionDialog(primaryStage, "Load Repo Task Failed", saveRepoTask.getException()).show();
        });
        saveRepoTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            notificationCentre.publish(SpringAppEvents.UPDATE_STAGE_TITLE, "AddressApp - " + StringUtils.replaceLast(repoPath.getFileName().toString(), ".json", ""));
            MFXAnimationFactory.FADE_OUT.build(changesLabel, 600).play();
            savingDialog.close();
        });

        return saveRepoTask;
    }

    private void setupWindowControls() {
        closeIcon.setIcon(new MFXFontIcon("mfx-x"));
        minimizeIcon.setIcon(new MFXFontIcon("mfx-minus", 12));

        NodeUtils.makeRegionCircular(closeIcon);
        NodeUtils.makeRegionCircular(minimizeIcon);

        minimizeIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> primaryStage.setIconified(true));
    }

    @FXML
    void handleNew() {
        repo.getPeople().clear();
        DAOUtils.setRepoPath(null);
        notificationCentre.publish(SpringAppEvents.CHANGES_OCCURRED);
        notificationCentre.publish(SpringAppEvents.UPDATE_STAGE_TITLE, "AddressApp - New File");
    }

    @FXML
    void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            TaskHelper.runTask(loadRepoTask(file.toPath()));
        }
    }

    @FXML
    void handleSave() {
        Path repoPath = DAOUtils.readRepoPath();
        if (repoPath.toString().trim().isEmpty()) {
            handleSaveAs();
        } else {
            TaskHelper.runTask(saveRepoTask(repoPath));
        }
    }

    @FXML
    void handleSaveAs() {
        Path repoPath = saveAsCommon();
        if (repoPath != null) {
            TaskHelper.runTask(saveRepoTask(repoPath));
        }
    }

    @FXML
    private void handleShowStats() {
        ChartDialog dialog = new ChartDialog();
        dialog.setData(repo.getPeople());
        MFXStageDialog stageDialog = new MFXStageDialog(dialog);
        stageDialog.setOwner(primaryStage);
        stageDialog.setModality(Modality.APPLICATION_MODAL);
        stageDialog.show();
    }

    @FXML
    private void handleAbout() {
        try {
            MFXDialog infoDialog = (MFXDialog) infoDialogView.getView();
            MFXStageDialog stageDialog = new MFXStageDialog(infoDialog);
            stageDialog.setOwner(primaryStage);
            stageDialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleExit() {
        if (changesLabel.getOpacity() != 0) {
            showExitDialog();
        } else {
            Platform.exit();
        }
    }

    private void showExitDialog() {
        ExitDialog exitDialog = new ExitDialog();

        MFXStageDialog stageDialog = new MFXStageDialog(exitDialog);
        stageDialog.setOwner(primaryStage);
        stageDialog.setModality(Modality.APPLICATION_MODAL);
        stageDialog.show();

        exitDialog.setSaveAction(event -> {
            Path repoPath = DAOUtils.readRepoPath();
            if (repoPath.toString().trim().isEmpty()) {
                Path saveAsPath = saveAsCommon();
                if (saveAsPath != null) {
                    Task<Boolean> saveTask = saveRepoTask(saveAsPath);
                    saveTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, taskEvent -> stageDialog.close());
                    TaskHelper.runTask(saveTask);
                }
            } else {
                Task<Boolean> saveTask = saveRepoTask(repoPath);
                saveTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, taskEvent -> stageDialog.close());
                TaskHelper.runTask(saveTask);
            }
        });

        exitDialog.setSaveExitAction(event -> {
            Path repoPath = DAOUtils.readRepoPath();
            if (repoPath.toString().trim().isEmpty()) {
                Path saveAsPath = saveAsCommon();
                if (saveAsPath != null) {
                    Task<Boolean> saveTask = saveRepoTask(saveAsPath);
                    saveTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, taskEvent -> Platform.exit());
                    TaskHelper.runTask(saveTask);
                }
            } else {
                Task<Boolean> saveTask = saveRepoTask(repoPath);
                saveTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, taskEvent -> Platform.exit());
                TaskHelper.runTask(saveTask);
            }
        });
        exitDialog.setExitAction(event -> Platform.exit());
    }

    private Path saveAsCommon() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "JSON files (*.json)", "*.json"
        );
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            Path repoPath = file.toPath();
            if (!repoPath.endsWith(".json")) {
                repoPath.resolve(".json");
            }
            return repoPath;
        }
        return null;
    }
}
