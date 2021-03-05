package io.github.palexdev.addressapp.utils;

import io.github.palexdev.addressapp.SpringApp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

public class DAOUtils {

    private DAOUtils() {}

    public static Path readRepoPath() {
        Preferences prefs = Preferences.userNodeForPackage(SpringApp.class);
        return Paths.get(prefs.get("repoPath", ""));
    }

    public static void setRepoPath(Path path) {
        Preferences prefs = Preferences.userNodeForPackage(SpringApp.class);
        if (path != null) {
            prefs.put("repoPath", path.toString());
        } else {
            prefs.remove("repoPath");
        }
    }
}
