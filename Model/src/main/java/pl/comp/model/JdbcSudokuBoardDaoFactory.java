package pl.comp.model;


import pl.comp.model.exceptions.SudokuDaoException;

public class JdbcSudokuBoardDaoFactory implements DaoFactory<SudokuBoard> {
    @Override
    public Dao<SudokuBoard> getDao(String dbName) throws SudokuDaoException {
        String url = "jdbc:h2:./" + dbName;
        return new JdbcSudokuBoardDao(url);
    }
}
