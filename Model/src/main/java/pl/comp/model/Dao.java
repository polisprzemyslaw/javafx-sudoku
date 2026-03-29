package pl.comp.model;

import pl.comp.model.exceptions.SudokuDaoException;

import java.util.List;

public interface Dao<T> extends AutoCloseable {
    T read(String name) throws SudokuDaoException;

    void write(String name, T obj) throws SudokuDaoException;

    List<String> names() throws SudokuDaoException;

}
