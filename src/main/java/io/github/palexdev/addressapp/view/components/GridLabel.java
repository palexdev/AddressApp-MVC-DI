package io.github.palexdev.addressapp.view.components;

import io.github.palexdev.addressapp.ResourceManager;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.enums.Styles;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;

public class GridLabel extends MFXLabel {
    private final String STYLE_CLASS = "grid-label";
    private final String STYLESHEET = ResourceManager.loadResource("css/GridLabel.css");

    private static final PseudoClass EDITABLE_PSEUDO_CLASS = PseudoClass.getPseudoClass("editable");

    public GridLabel() {
        setLabelStyle(Styles.LabelStyles.STYLE2);
        initialize();
    }

    public GridLabel(String text) {
        super(text);
        setLabelStyle(Styles.LabelStyles.STYLE2);
        initialize();
    }

    private void initialize() {
        getStyleClass().add(STYLE_CLASS);
        getStylesheets().setAll(STYLESHEET);
        setPrefWidth(250);
        setMinWidth(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setPromptText("");
        setAlignment(Pos.CENTER_LEFT);

        editableProperty().addListener(invalidate -> pseudoClassStateChanged(EDITABLE_PSEUDO_CLASS, editableProperty().get()));
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }
}
