package io.github.palexdev.addressapp;

import java.net.URL;

public class ResourceManager {

    private ResourceManager() {}

    public static URL getResource(String name) {
        return ResourceManager.class.getResource(name);
    }

    public static String loadResource(String name) {
        return getResource(name).toString();
    }
}
