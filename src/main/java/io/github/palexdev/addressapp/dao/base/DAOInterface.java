package io.github.palexdev.addressapp.dao.base;

import io.github.palexdev.addressapp.dao.DAOException;

import java.nio.file.Path;

public interface DAOInterface {
    void loadRepository(Path filePath) throws DAOException;
    void saveToFile(Path filePath) throws DAOException;
}
