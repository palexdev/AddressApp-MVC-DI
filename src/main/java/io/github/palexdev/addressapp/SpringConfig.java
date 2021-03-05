package io.github.palexdev.addressapp;

import com.gluonhq.ignite.spring.SpringContext;
import io.github.palexdev.addressapp.dao.PeopleDAO;
import io.github.palexdev.addressapp.dao.base.DAOInterface;
import io.github.palexdev.addressapp.events.EnumNotificationCentre;
import io.github.palexdev.addressapp.events.StringNotificationCentre;
import io.github.palexdev.addressapp.events.base.IEnumNotificationCentre;
import io.github.palexdev.addressapp.events.base.IStringNotificationCentre;
import io.github.palexdev.addressapp.view.InfoDialogView;
import io.github.palexdev.addressapp.view.MainView;
import io.github.palexdev.addressapp.view.PersonOverview;
import io.github.palexdev.addressapp.view.ViewModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringConfig {

    public static void initContext(Object contextRoot) {
        String basePackage = contextRoot.getClass().getPackage().getName();
        new SpringContext(contextRoot, () -> List.of(basePackage)).init();
    }

    @Bean
    public SpringHelper getSpringHelper() {
        return new SpringHelper();
    }

    @Bean
    public MainView getMainView() {
        return new MainView();
    }

    @Bean
    public PersonOverview getPersonOverview() {
        return new PersonOverview();
    }

    @Bean
    public InfoDialogView getInfoDialog() {
        return new InfoDialogView();
    }

    @Bean
    public ViewModel getViewModel() {
        return new ViewModel();
    }

    @Bean
    public DAOInterface getPeopleDAO() {
        return new PeopleDAO();
    }

    @Bean
    public IStringNotificationCentre getStringNotificationCentre() {
        return new StringNotificationCentre();
    }

    @Bean
    public IEnumNotificationCentre<?> gEnumNotificationCentre() {
        return new EnumNotificationCentre<>();
    }
}
