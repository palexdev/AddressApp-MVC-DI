package io.github.palexdev.addressapp.view.base;

import javafx.scene.Parent;

import java.io.IOException;

public interface View {
    Parent getView() throws IOException;
}
