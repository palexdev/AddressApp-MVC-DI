package io.github.palexdev.addressapp;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpringHelper {

    @Inject
    private AnnotationConfigApplicationContext context;

    public void forceInject(Object object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
    }

    public AnnotationConfigApplicationContext context() {
        return context;
    }
}
