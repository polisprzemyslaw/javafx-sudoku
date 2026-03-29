package pl.comp.model;

public class FileSudokuBoardDaoFactory implements DaoFactory<SudokuBoard> {

    public FileSudokuBoardDaoFactory() {
    }

    public Dao<SudokuBoard> getDao(String dirName) {
        return new FileSudokuBoardDao(dirName);
    }

}
