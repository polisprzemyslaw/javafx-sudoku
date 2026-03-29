package pl.comp.model;

import pl.comp.model.exceptions.SudokuDaoException;

public interface DaoFactory<T> {
    Dao<T> getDao(String name) throws SudokuDaoException;
}
