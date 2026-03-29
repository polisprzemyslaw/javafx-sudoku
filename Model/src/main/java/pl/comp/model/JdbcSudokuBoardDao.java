package pl.comp.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.comp.model.exceptions.SudokuDaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {
    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(JdbcSudokuBoardDao.class);

    public JdbcSudokuBoardDao(String url) throws SudokuDaoException {
        try {
            this.connection = DriverManager.getConnection(url, "sa", "");
            createTables();
        } catch (SQLException e) {
            throw new SudokuDaoException("error.database.connection");
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS SudokuBoards ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "board_name VARCHAR(255) NOT NULL UNIQUE)"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS SudokuFields ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "board_id INT NOT NULL, "
                            + "X INT NOT NULL, "
                            + "Y INT NOT NULL, "
                            + "field_value INT NOT NULL, "
                            + "FOREIGN KEY (board_id) REFERENCES SudokuBoards(id) ON DELETE CASCADE)"
            );
        }
    }


    @Override
    public SudokuBoard read(String boardName) throws SudokuDaoException {
        SudokuBoard board = new SudokuBoard();
        int foundCounter = 0;

        String query = "SELECT SudokuFields.X, SudokuFields.Y, SudokuFields.field_value FROM SudokuFields "
                + "JOIN SudokuBoards ON SudokuFields.board_id = SudokuBoards.id "
                + "WHERE SudokuBoards.board_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, boardName);
            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    foundCounter++;
                    int x = result.getInt("X");
                    int y = result.getInt("Y");
                    int value = result.getInt("field_value");

                    board.setField(x, y, value);
                }
            }

        } catch (SQLException e) {
            logger.error("Database Read Error: {}", e.getMessage());
            throw new SudokuDaoException("error.database.read", e);
        }

        if (foundCounter != 81) {
            logger.error("Database Read Error: Invalid field count");
            throw new SudokuDaoException("error.database.structure");
        }

        return board;
    }

    @Override
    public void write(String name, SudokuBoard board) throws SudokuDaoException {
        String boardInsertQuery = "INSERT INTO SudokuBoards (board_name) VALUES (?)";
        String fieldInsertQuery = "INSERT INTO SudokuFields (board_id, X, Y, field_value) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int generatedBoardId = -1;

            try (PreparedStatement boardStatement =
                         connection.prepareStatement(boardInsertQuery, Statement.RETURN_GENERATED_KEYS)) {
                boardStatement.setString(1, name);
                boardStatement.executeUpdate();

                try (ResultSet generatedKeys = boardStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedBoardId = generatedKeys.getInt(1);
                    }
                }
            }

            if (generatedBoardId == -1) {
                logger.error("Database Write Error: Failed to generate board ID");
                throw new SudokuDaoException("error.database.id");
            }

            try (PreparedStatement fieldStatement = connection.prepareStatement(fieldInsertQuery)) {
                for (int x = 0; x < 9; x++) {
                    for (int y = 0; y < 9; y++) {
                        fieldStatement.setInt(1, generatedBoardId);
                        fieldStatement.setInt(2, x);
                        fieldStatement.setInt(3, y);
                        fieldStatement.setInt(4, board.getField(x, y));
                        fieldStatement.addBatch();
                    }
                }
                fieldStatement.executeBatch();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Rollback failed",rollbackEx);
                throw new SudokuDaoException("error.database.write");
            }
            logger.error("Database Write Error: {}",e.getMessage());
            throw new SudokuDaoException("error.database.write");
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Database Write Error: {}",e.getMessage());
            }
        }
    }

    @Override
    public List<String> names() throws SudokuDaoException {
        List<String> names = new ArrayList<>();
        String sql = "SELECT board_name FROM SudokuBoards";

        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                names.add(result.getString("board_name"));
            }

        } catch (SQLException e) {
            logger.error("Database Listing Error: {}",e.getMessage());
            throw new SudokuDaoException("error.database.list",e);
        }

        return names;
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
