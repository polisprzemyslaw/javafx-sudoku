package pl.comp.model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.comp.model.exceptions.SudokuDaoException;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileSudokuBoardDaoTest {
    @Test
    public void FileSudokuBoardDaoKeepsValueTest() throws SudokuDaoException {
        SudokuBoard testBoard1 = new SudokuBoard();
        testBoard1.solveGame();

        DaoFactory<SudokuBoard> factory = new FileSudokuBoardDaoFactory();
        try (Dao<SudokuBoard> dao = factory.getDao("sudoku_test_files")) {
            dao.write("test_board1", testBoard1);
            SudokuBoard testBoard2 = dao.read("test_board1");
            assertNotSame(testBoard1, testBoard2);
            assertEquals(testBoard1, testBoard2);
            dao.write("test_board2", testBoard2);
        } catch (Exception e) {
            throw new SudokuDaoException("Dao IO error", e);
        }
    }

    @Test
    public void FileSudokuBoardDaoSolverTest() throws SudokuDaoException {
        SudokuBoard testBoard = new SudokuBoard();
        DaoFactory<SudokuBoard> factory = new FileSudokuBoardDaoFactory();
        try (Dao<SudokuBoard> dao = factory.getDao("sudoku_test_files")) {
            dao.write("test_board", testBoard);
            testBoard = dao.read("test_board");
            testBoard.solveGame();
            assertNotEquals(0, testBoard.getField(4,5));
        } catch (Exception e) {
            throw new SudokuDaoException("Dao IO error", e);
        }
    }

    @Test
    public void FileSudokuBoardDaoNamesTest() throws SudokuDaoException {
        SudokuBoard testBoard = new SudokuBoard();
        DaoFactory<SudokuBoard> factory = new FileSudokuBoardDaoFactory();
        try (Dao<SudokuBoard> dao = factory.getDao("sudoku_test_files")) {
            assertTrue(dao.names().isEmpty());
            dao.write("test_board1", testBoard);
            dao.write("test_board2", testBoard);
            dao.write("test_board3", testBoard);
            File dir1 = new File("sudoku_test_files/directory");
            dir1.mkdir();
            assertTrue(dao.names().contains("test_board1"));
            assertTrue(dao.names().contains("test_board2"));
            assertTrue(dao.names().contains("test_board3"));
            assertFalse(dao.names().contains("directory"));
        } catch (Exception e) {
            throw new SudokuDaoException("Dao IO error", e);
        }
    }

    @Test
    public void FileSudokuBoardExceptionTest() throws SudokuDaoException {
        SudokuBoard testBoard = new SudokuBoard();
        DaoFactory<SudokuBoard> factory = new FileSudokuBoardDaoFactory();
        try (Dao<SudokuBoard> dao = factory.getDao("sudoku_test_files")) {
            dao.write("test_board", testBoard);
            File dir1 = new File("sudoku_test_files/directory");
            dir1.mkdir();
            assertThrows(SudokuDaoException.class, () -> dao.read("directory"));
            assertThrows(SudokuDaoException.class, () -> dao.read("does_not_exist"));

            try (FileOutputStream f = new FileOutputStream("sudoku_test_files/string_file");
                 ObjectOutputStream s = new ObjectOutputStream(f)) {
                s.writeObject("teststring");
            } catch (Exception e) {
                throw new SudokuDaoException("Dao IO error", e);
            }

            assertThrows(SudokuDaoException.class, () -> dao.read("string_file"));
            assertThrows(SudokuDaoException.class, () -> dao.write("directory", testBoard));
        } catch (Exception e) {
            throw new SudokuDaoException("Dao IO error", e);
        }
    }

    @Test
    public void AutocloseableTest() throws Exception {
        var testDao1 = new FileSudokuBoardDao("sudoku_test_files") {
            boolean isClosed = false;

            @Override
            public void close() {
                super.close();
                isClosed = true;
            }

            public boolean wasClosed() {
                return isClosed;
            }
        };

        try (testDao1) {
            testDao1.write("test1", new SudokuBoard());
        }

        assertTrue(testDao1.wasClosed());

        var testDao2 = new FileSudokuBoardDao("sudoku_test_files") {
            boolean isClosed = false;

            @Override
            public void close() {
                super.close();
                isClosed = true;
            }

            public boolean wasClosed() {
                return isClosed;
            }
        };

        try (testDao2) {
            testDao2.write("test2", new SudokuBoard());
            throw new Exception();
        } catch (Exception e) {}
        assertTrue(testDao2.wasClosed());
    }

    @AfterEach
    public void DeleteFilesAndVerifyIfClosed() {
        File dir = new File("sudoku_test_files");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                assertTrue(file.delete());
            }
        }
        if (dir.exists()) {
            assertTrue(dir.delete());
        }
    }
}