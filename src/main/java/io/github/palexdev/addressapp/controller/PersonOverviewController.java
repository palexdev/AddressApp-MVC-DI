package io.github.palexdev.addressapp.controller;

import io.github.palexdev.addressapp.SpringHelper;
import io.github.palexdev.addressapp.events.EnumNotificationCentre;
import io.github.palexdev.addressapp.events.SpringAppEvents;
import io.github.palexdev.addressapp.model.PeopleRepository;
import io.github.palexdev.addressapp.model.Person;
import io.github.palexdev.addressapp.view.ViewModel;
import io.github.palexdev.addressapp.view.components.AddEditDialog;
import io.github.palexdev.addressapp.view.components.PersonGrid;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXStageDialog;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableColumnCell;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.selection.ITableSelectionModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PersonOverviewController implements Initializable {
    private double xOffset;
    private double yOffset;

    private PersonGrid grid;

    @Inject
    private Stage primaryStage;

    @FXML
    private SplitPane pane;

    @FXML
    private MFXLabel repoLabel;

    @FXML
    private MFXLabel personLabel;

    @FXML
    private MFXTableView<Person> table;

    @FXML
    private StackPane gridContainer;

    @FXML
    private MFXButton addButton;

    @FXML
    private MFXButton removeButton;

    @FXML
    private MFXButton editButton;

    @Inject
    private SpringHelper helper;

    @Inject
    private PeopleRepository repo;

    @Inject
    private ViewModel viewModel;

    @Inject
    private EnumNotificationCentre<SpringAppEvents> notificationCentre;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initGrid();
        initActions();
        initNotifications();

        repoLabel.setLeadingIcon(new MFXFontIcon("mfx-users", 12));
        personLabel.setLeadingIcon(new MFXFontIcon("mfx-user", 12));

        pane.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        pane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
    }

    @SuppressWarnings("unchecked")
    private void initTable() {
        MFXTableColumnCell<Person> firstNameColumn = new MFXTableColumnCell<>("First Name");
        MFXTableColumnCell<Person> lastNameColumn = new MFXTableColumnCell<>("Last Name");
        MFXTableColumnCell<Person> cityColumn = new MFXTableColumnCell<>("City");
        MFXTableColumnCell<Person> streetColumn = new MFXTableColumnCell<>("Street");
        MFXTableColumnCell<Person> postalCodeColumn = new MFXTableColumnCell<>("Postal Code");
        MFXTableColumnCell<Person> birthdayColumn = new MFXTableColumnCell<>("Birthday");

        firstNameColumn.setRowCellFactory(person -> new MFXTableRowCell(person.firstNameProperty()));
        lastNameColumn.setRowCellFactory(person -> new MFXTableRowCell(person.lastNameProperty()));
        cityColumn.setRowCellFactory(person -> new MFXTableRowCell(person.cityProperty()));
        streetColumn.setRowCellFactory(person -> new MFXTableRowCell(person.streetProperty()));
        postalCodeColumn.setRowCellFactory(person -> {
            MFXTableRowCell cell = new MFXTableRowCell(person.postalCodeProperty().asString());
            cell.setAlignment(Pos.CENTER_RIGHT);
            return cell;
        });
        birthdayColumn.setRowCellFactory(person -> {
            MFXTableRowCell cell = new MFXTableRowCell(
                    Bindings.createStringBinding(() -> person.getBirthday().format(DateTimeFormatter.ofPattern("d/M/yyyy")), person.birthdayProperty())
            );
            cell.setAlignment(Pos.CENTER_RIGHT);
            return cell;
        });

        postalCodeColumn.setAlignment(Pos.CENTER_RIGHT);
        birthdayColumn.setAlignment(Pos.CENTER_RIGHT);

        table.getColumns().addAll(
                firstNameColumn,
                lastNameColumn,
                cityColumn,
                streetColumn,
                postalCodeColumn,
                birthdayColumn
        );

        ITableSelectionModel<Person> selectionModel = table.getSelectionModel();
        selectionModel.setAllowsMultipleSelection(false);
        selectionModel.getSelectedRows().addListener((observable, oldValue, newValue) -> {
            Person p = selectionModel.getSelectedRow() != null ? selectionModel.getSelectedRow().getItem() : null;
            viewModel.setSelectedPerson(p);
        });
        table.setItems(repo.getPeople());
    }

    private void initGrid() {
        grid = new PersonGrid();
        helper.forceInject(grid);
        grid.setLabels(viewModel.getSelectedPerson());
        gridContainer.getChildren().setAll(grid);
    }

    private void initActions() {
        editButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> viewModel.getSelectedPerson() == null, viewModel.selectedPersonProperty()
        ));
        removeButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> viewModel.getSelectedPerson() == null, viewModel.selectedPersonProperty()
        ));

        addButton.setOnAction(event -> createDialog().show());
        editButton.setOnAction(event -> createDialog().show());
        removeButton.setOnAction(event -> {
            repo.getPeople().remove(viewModel.getSelectedPerson());
            notificationCentre.publish(SpringAppEvents.CHANGES_OCCURRED);
        });
    }

    private void initNotifications() {
        notificationCentre.subscribe(SpringAppEvents.REFRESH_GRID_EVENT, (messageType, payload) -> grid.refresh());
        notificationCentre.subscribe(SpringAppEvents.MENU_ADD, (messageType, payload) -> createDialog().show());
        notificationCentre.subscribe(SpringAppEvents.MENU_REMOVE, (messageType, payload) -> {
            if (viewModel.getSelectedPerson() != null) {
                repo.getPeople().remove(viewModel.getSelectedPerson());
                notificationCentre.publish(SpringAppEvents.CHANGES_OCCURRED);
            }
        });
        notificationCentre.subscribe(SpringAppEvents.MENU_EDIT, (messageType, payload) -> {
            if (viewModel.getSelectedPerson() != null) {
                createDialog().show();
            }
        });
    }

    private MFXStageDialog createDialog() {
        AddEditDialog addEditDialog = new AddEditDialog();
        helper.forceInject(addEditDialog);
        MFXStageDialog stageDialog = new MFXStageDialog(addEditDialog);
        stageDialog.setAllowDrag(false);
        stageDialog.setOwner(primaryStage);
        stageDialog.setModality(Modality.WINDOW_MODAL);
        stageDialog.show();
        return stageDialog;
    }
}
