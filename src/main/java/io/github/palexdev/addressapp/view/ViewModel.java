package io.github.palexdev.addressapp.view;

import io.github.palexdev.addressapp.model.Person;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.inject.Singleton;

@Singleton
public class ViewModel {
    private final ObjectProperty<Person> selectedPerson = new SimpleObjectProperty<>();

    public Person getSelectedPerson() {
        return selectedPerson.get();
    }

    public ObjectProperty<Person> selectedPersonProperty() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson.set(selectedPerson);
    }
}
