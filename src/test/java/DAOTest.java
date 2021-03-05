import io.github.palexdev.addressapp.SpringConfig;
import io.github.palexdev.addressapp.utils.DialogHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXStageDialog;
import io.github.palexdev.materialfx.controls.enums.DialogType;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DAOTest.class)
@Import({SpringConfig.class, TestConfig.class})
public class DAOTest extends ApplicationTest {
    private final FxRobot fxRobot= new FxRobot();
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        FxToolkit.registerPrimaryStage();
        this.stage = stage;
        StackPane stackPane = new StackPane(new MFXButton());
        Scene scene = new Scene(stackPane, 100, 100);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testInject() {
        AtomicReference<MFXStageDialog> stageDialog = new AtomicReference<>();
        fxRobot.interact(() -> stageDialog.set(DialogHelper.getStageDialog(stage, DialogType.INFO, "Successful Test Injection", "Even this works...")));
        try {
            MFXStageDialog sd = stageDialog.get();
            Field field = sd.getClass().getDeclaredField("dialogStage");
            field.setAccessible(true);
            Stage value = (Stage) field.get(sd);
            Assertions.assertEquals(stage, value.getOwner());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
