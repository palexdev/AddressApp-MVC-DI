package io.github.palexdev.addressapp.view.components;

import io.github.palexdev.addressapp.ResourceManager;
import io.github.palexdev.addressapp.model.Person;
import io.github.palexdev.addressapp.view.ViewModel;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersonGrid extends GridPane {
    private final String STYLE_CLASS = "person-grid";
    private final String STYLESHEET = ResourceManager.loadResource("css/PersonGrid.css");

    private final GridLabel firstNameLabel;
    private final GridLabel lastNameLabel;
    private final GridLabel cityLabel;
    private final GridLabel streetLabel;
    private final GridLabel postaCodeLabel;
    private final GridLabel birthdayLabel;

    private ViewModel viewModel;

    public PersonGrid() {
        firstNameLabel = new GridLabel();
        lastNameLabel = new GridLabel();
        cityLabel = new GridLabel();
        streetLabel = new GridLabel();
        postaCodeLabel = new GridLabel();
        birthdayLabel = new GridLabel();

        initialize();
    }

    @PostConstruct
    @Inject
    private void post(ViewModel viewModel) {
        this.viewModel = viewModel;
        setLabels(viewModel.getSelectedPerson());
        viewModel.selectedPersonProperty().addListener((observable, oldValue, newValue) -> setLabels(newValue));
    }

    private void initialize() {
        getStyleClass().add(STYLE_CLASS);
        getColumnConstraints().setAll(buildColumnConstraints());
        getRowConstraints().setAll(buildRowConstraints());
        populateGrid();
    }

    public PersonGrid setEditable(boolean editable) {
        firstNameLabel.setEditable(editable);
        lastNameLabel.setEditable(editable);
        cityLabel.setEditable(editable);
        streetLabel.setEditable(editable);
        postaCodeLabel.setEditable(editable);
        birthdayLabel.setEditable(editable);
        return this;
    }

    private Collection<ColumnConstraints> buildColumnConstraints() {
        List<ColumnConstraints> columnConstraints = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100);
            columnConstraints.add(cc);
        }
        return columnConstraints;
    }

    private Collection<RowConstraints> buildRowConstraints() {
        List<RowConstraints> rowConstraints = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(18);
            rowConstraints.add(rc);
        }
        return rowConstraints;
    }

    private void populateGrid() {
        add(new GridLabel("First Name"), 0, 0);
        add(new GridLabel("Last Name"), 0, 1);
        add(new GridLabel("City"), 0, 2);
        add(new GridLabel("Street"), 0, 3);
        add(new GridLabel("Postal Code"), 0, 4);
        add(new GridLabel("Birthday"), 0, 5);

        add(firstNameLabel, 1, 0);
        add(lastNameLabel, 1, 1);
        add(cityLabel, 1, 2);
        add(streetLabel, 1, 3);
        add(postaCodeLabel, 1, 4);
        add(birthdayLabel, 1, 5);
    }

    public void setLabels(Person person) {
        if (person != null) {
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            cityLabel.setText(person.getCity());
            streetLabel.setText(person.getStreet());
            postaCodeLabel.setText(Integer.toString(person.getPostalCode()));
            birthdayLabel.setText(person.getBirthday().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
        } else {
            setPrompt();
        }
    }

    public void setPrompt() {
        firstNameLabel.setPromptText("First name");
        lastNameLabel.setPromptText("Last name");
        cityLabel.setPromptText("City");
        streetLabel.setPromptText("Street");
        postaCodeLabel.setPromptText("Postal code (Integer)");
        birthdayLabel.setPromptText("Birthday (format: d/M/YYYY");
    }

    public void refresh() {
        setLabels(viewModel.getSelectedPerson());
    }

    private String errorsCheck() {
        StringBuilder errors = new StringBuilder("\n");

        try {
            Integer.parseInt(postaCodeLabel.getText());
        } catch (NumberFormatException nfe) {
            errors.append("Postal Code must be an integer").append("\n");
        }

        try {
            LocalDate.parse(birthdayLabel.getText(), DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch (DateTimeParseException dtpe) {
            errors.append("The provided date is invalid (format: d/M/yyyy)").append("\n");
        }

        return errors.toString();
    }

    public Person buildPerson() throws IllegalArgumentException {
        String errors = errorsCheck();
        if (!errors.trim().isEmpty()) {
            throw new IllegalArgumentException(errors);
        }

        String fn = firstNameLabel.getText();
        String ln = lastNameLabel.getText();
        String city = cityLabel.getText();
        String street = streetLabel.getText();
        int postalCode = Integer.parseInt(postaCodeLabel.getText());
        LocalDate birthday = LocalDate.parse(birthdayLabel.getText(), DateTimeFormatter.ofPattern("d/M/yyyy"));

        return new Person(fn, ln, city, street, postalCode, birthday);
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }
}
