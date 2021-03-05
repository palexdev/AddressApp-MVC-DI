import javafx.fxml.FXMLLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public FXMLLoader getFxmlLoader() {
        return new FXMLLoader();
    }
}
