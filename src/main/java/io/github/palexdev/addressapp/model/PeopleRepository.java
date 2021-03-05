package io.github.palexdev.addressapp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class PeopleRepository {
    private final ObservableList<Person> people = FXCollections.observableArrayList();

    public void addPerson(Person person) {
        people.add(person);
    }

    public void addPeople(Collection<Person> people) {
        this.people.addAll(people);
    }

    public ObservableList<Person> getPeople() {
        return people;
    }
}
