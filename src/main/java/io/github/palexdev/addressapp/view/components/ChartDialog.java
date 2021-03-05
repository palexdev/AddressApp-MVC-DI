package io.github.palexdev.addressapp.view.components;

import io.github.palexdev.addressapp.model.Person;
import io.github.palexdev.materialfx.controls.MFXDialog;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ChartDialog extends MFXDialog {
    private final String [] months = DateFormatSymbols.getInstance(Locale.getDefault()).getMonths();
    private final BarChart<String, Number> barChart;
    private final MFXFontIcon closeIcon;

    public ChartDialog() {
        setPrefSize(600, 600);
        closeIcon = new MFXFontIcon("mfx-x", 12);
        closeIcon.setManaged(false);
        setCloseButtons(closeIcon);

        CategoryAxis xAxis = new CategoryAxis(FXCollections.observableArrayList(months));

        barChart = new BarChart<>(xAxis, new NumberAxis());

        setCenter(barChart);
        getChildren().add(closeIcon);
    }

    public void setData(List<Person> people) {
        int[] monthCounter = new int[12];
        for (Person person : people) {
            int month = person.getBirthday().getMonthValue() - 1;
            monthCounter[month]++;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(months[i], monthCounter[i]));
        }
        barChart.getData().add(series);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        closeIcon.resizeRelocate(getWidth() - 20, 10, 20, 20);
    }
}
