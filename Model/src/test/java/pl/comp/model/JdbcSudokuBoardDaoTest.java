package pl.comp.model;
import org.junit.jupiter.api.Test;
import pl.comp.model.exceptions.SudokuDaoException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcSudokuBoardDaoTest {

    @Test
    void JdbcWriteAndReadTest() throws SudokuDaoException {

        SudokuBoard testBoard1 = new SudokuBoard();
        testBoard1.solveGame();
        String boardName = "TestBoard";

        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("jdbc:h2:mem:testdb")) {
            dao.write(boardName, testBoard1);
            SudokuBoard testBoard2 = dao.read(boardName);

            assertNotNull(testBoard2);
            assertNotSame(testBoard1, testBoard2);
            assertEquals(testBoard1, testBoard2);
        } catch (Exception e) {
            throw new SudokuDaoException("Dao IO error", e);
        }
    }

    @Test
    void testRollback() throws Exception {
        String boardName = "TestBoard";
        SudokuBoard board = new SudokuBoard();
        board.solveGame();

        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("jdbc:h2:mem:testdb")) {

            try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
                 Statement stmt = conn.createStatement()) {
                stmt.execute("ALTER TABLE SudokuFields ADD CONSTRAINT test CHECK (X < 0)");
            }

            assertThrows(SudokuDaoException.class, () -> {
                dao.write(boardName, board);
            });

            List<String> names = dao.names();
            assertTrue(names.isEmpty());
        }
    }

    @Test
    void testExceptionHandling() throws Exception {
        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("jdbc:h2:mem:testdb")) {

            SudokuDaoException readEx = assertThrows(SudokuDaoException.class, () -> {
                dao.read("nie_istniejaca_plansza");
            });
            assertEquals("error.database.structure", readEx.getMessage());

            SudokuBoard board = new SudokuBoard();
            dao.write("TestBoard", board);

            SudokuDaoException writeEx = assertThrows(SudokuDaoException.class, () -> {
                dao.write("TestBoard", board);
            });
            assertEquals("error.database.write", writeEx.getMessage());
        }
    }

    @Test
    void testBoardNameList() throws SudokuDaoException {

        SudokuBoard testBoard1 = new SudokuBoard();
        testBoard1.solveGame();
        String boardName = "TestBoard";

        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("jdbc:h2:mem:testdb")) {
            dao.write(boardName, testBoard1);

            assertNotNull(dao.names().getFirst());
            assertEquals(boardName, dao.names().getFirst());
        } catch (Exception e) {
            throw new SudokuDaoException("Dao IO error", e);
        }
    }
}