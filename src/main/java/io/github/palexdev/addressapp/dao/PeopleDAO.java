package io.github.palexdev.addressapp.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.palexdev.addressapp.dao.base.DAOInterface;
import io.github.palexdev.addressapp.model.PeopleRepository;
import io.github.palexdev.addressapp.model.Person;
import io.github.palexdev.addressapp.utils.DAOUtils;
import javafx.application.Platform;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PeopleDAO implements DAOInterface {
    public final GsonBuilder gsonBuilder;

    @Inject
    private PeopleRepository repo;

    public PeopleDAO() {
        this.gsonBuilder = new GsonBuilder();
    }

    @Override
    public void loadRepository(Path filePath) throws DAOException {
        if (filePath == null) {
            return;
        }

        Gson gson = gsonBuilder
                .registerTypeAdapter(Person.class, new PersonAdapter())
                .create();

        List<Person> people;
        try (Reader reader = new FileReader(filePath.toFile())) {
            people = new ArrayList<>((gson.fromJson(reader, new TypeToken<List<Person>>() {}.getType())));
            Platform.runLater(() -> repo.getPeople().setAll(people));
            DAOUtils.setRepoPath(filePath);
        } catch (Exception ex) {
            throw new DAOException("Cannot load json file: " + filePath + "...    " + ex.getMessage());
        }
    }

    @Override
    public void saveToFile(Path filePath) throws DAOException {
        Gson gson = gsonBuilder
                .registerTypeAdapter(Person.class, new PersonAdapter())
                .setPrettyPrinting()
                .create();

        List<Person> people = repo.getPeople();

        try (Writer writer = new FileWriter(filePath.toFile())) {
            gson.toJson(people, writer);
            writer.flush();
            DAOUtils.setRepoPath(filePath);
        } catch (Exception ex) {
            throw new DAOException("Cannot save repository to json file! " + "Path: " + filePath.toString() + "\n" + ex.getMessage());
        }
    }
}
