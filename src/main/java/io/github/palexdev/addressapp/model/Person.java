package io.github.palexdev.addressapp.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Objects;

public class Person {
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty city = new SimpleStringProperty();
    private final StringProperty street = new SimpleStringProperty();
    private final IntegerProperty postalCode = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();

    public Person(String firstName, String lastName, String city, String street, Integer postalCode, LocalDate birthday) {
        setFirstName(firstName);
        setLastName(lastName);
        setCity(city);
        setStreet(street);
        setPostalCode(postalCode);
        setBirthday(birthday);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public int getPostalCode() {
        return postalCode.get();
    }

    public IntegerProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
    }

    public LocalDate getBirthday() {
        return birthday.get();
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public void setFrom(Person person) {
        setFirstName(person.getFirstName());
        setLastName(person.getLastName());
        setCity(person.getCity());
        setStreet(person.getStreet());
        setPostalCode(person.getPostalCode());
        setBirthday(person.getBirthday());
    }

    @Override
    public boolean equals(Object otherPerson) {
        if (otherPerson == null || getClass() != otherPerson.getClass()) return false;
        Person person = (Person) otherPerson;
        return getFirstName().equals(person.getFirstName()) &&
                getLastName().equals(person.getLastName()) &&
                getCity().equals(person.getCity()) &&
                getStreet().equals(person.getStreet()) &&
                getPostalCode() == person.getPostalCode() &&
                getBirthday().equals(person.getBirthday());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getCity(), getStreet(), getPostalCode(), getBirthday());
    }
}
