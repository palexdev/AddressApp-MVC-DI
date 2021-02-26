module AddressApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires javax.inject;
    requires ignite.spring;
    requires spring.context;

    exports io.github.palexdev.addressapp;
    exports io.github.palexdev.addressapp.view;
    exports io.github.palexdev.addressapp.view.base;
}