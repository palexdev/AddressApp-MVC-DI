package io.github.palexdev.addressapp.view.components;


import io.github.palexdev.addressapp.ResourceManager;
import io.github.palexdev.addressapp.SpringHelper;
import io.github.palexdev.addressapp.events.EnumNotificationCentre;
import io.github.palexdev.addressapp.events.SpringAppEvents;
import io.github.palexdev.addressapp.model.PeopleRepository;
import io.github.palexdev.addressapp.model.Person;
import io.github.palexdev.addressapp.utils.DialogHelper;
import io.github.palexdev.addressapp.view.ViewModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDialog;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.enums.ButtonType;
import io.github.palexdev.materialfx.controls.enums.DialogType;
import io.github.palexdev.materialfx.controls.enums.Styles;
import io.github.palexdev.materialfx.effects.DepthLevel;
import io.github.palexdev.materialfx.effects.RippleGenerator;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class AddEditDialog extends MFXDialog {
    private final String STYLESHEET = ResourceManager.loadResource("css/AddEditDialog.css");

    @Inject
    private SpringHelper helper;

    @Inject
    private Stage primaryStage;

    @Inject
    private PeopleRepository repo;

    @Inject
    private EnumNotificationCentre<SpringAppEvents> notificationCentre;

    private ViewModel viewModel;

    private final MFXIconWrapper closeIcon;
    private final MFXLabel dialogLabel;
    private PersonGrid grid;

    private final MFXButton okButton;
    private final MFXButton cancelButton;

    public AddEditDialog() {
        closeIcon = new MFXIconWrapper(new MFXFontIcon("mfx-x"), 20).addRippleGenerator();
        closeIcon.setManaged(false);
        NodeUtils.makeRegionCircular(closeIcon);
        RippleGenerator rg = closeIcon.getRippleGenerator();
        closeIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            rg.setGeneratorCenterX(event.getX());
            rg.setGeneratorCenterY(event.getY());
            rg.createRipple();
        });

        dialogLabel = new MFXLabel();
        dialogLabel.setLabelStyle(Styles.LabelStyles.STYLE2);
        dialogLabel.getStyleClass().add("dialog-label");
        dialogLabel.getStylesheets().setAll(STYLESHEET);
        dialogLabel.textProperty().bind(titleProperty());
        dialogLabel.setMaxWidth(Double.MAX_VALUE);

        okButton = new MFXButton("OK");
        okButton.setId("okButton");
        okButton.setPrefSize(120, 26);
        okButton.setButtonType(ButtonType.RAISED);
        okButton.setDepthLevel(DepthLevel.LEVEL1);
        okButton.setRippleRadius(40);
        okButton.getStylesheets().addAll(STYLESHEET);
        cancelButton = new MFXButton("Cancel");
        cancelButton.setId("cancelButton");
        cancelButton.setPrefSize(120, 26);
        cancelButton.setButtonType(ButtonType.RAISED);
        cancelButton.setDepthLevel(DepthLevel.LEVEL1);
        cancelButton.setRippleRadius(40);
        cancelButton.getStylesheets().addAll(STYLESHEET);

        HBox box = new HBox(20, okButton, cancelButton);
        box.setAlignment(Pos.CENTER_RIGHT);

        setTop(dialogLabel);
        setBottom(box);

        setPrefSize(700, 350);
        BorderPane.setMargin(dialogLabel, new Insets(0, 10, 10, 10));
        BorderPane.setMargin(box, new Insets(0, 10, 10, 10));

        setCloseButtons(closeIcon, cancelButton, okButton);
        okButton.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            okButton.requestFocus();
            Person toAddEdit;

            try {
                toAddEdit = grid.buildPerson();
            } catch (IllegalArgumentException ex) {
                event.consume();
                DialogHelper.getStageDialog(primaryStage, DialogType.ERROR, "Error parsing input from fields", ex.getMessage()).show();
                return;
            }

            if (viewModel.getSelectedPerson() == null) {
                repo.addPerson(toAddEdit);
            } else {
                if (viewModel.getSelectedPerson().equals(toAddEdit)) {
                    return;
                }
                viewModel.getSelectedPerson().setFrom(toAddEdit);
                notificationCentre.publish(SpringAppEvents.REFRESH_GRID_EVENT);
            }
            notificationCentre.publish(SpringAppEvents.CHANGES_OCCURRED);
        });

        getChildren().add(closeIcon);
    }

    @PostConstruct
    @Inject
    private void post(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.grid = new PersonGrid().setEditable(true);
        helper.forceInject(grid);
        grid.getStylesheets().addAll(STYLESHEET);
        setCenter(grid);
        BorderPane.setMargin(grid, new Insets(10));
        if (viewModel.getSelectedPerson() != null) {
            setTitle("Edit Person - Dialog");
        } else {
            setTitle("Add Person - Dialog");
        }
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        closeIcon.resizeRelocate(getWidth() - 34, 4, 20, 20);
    }
}
