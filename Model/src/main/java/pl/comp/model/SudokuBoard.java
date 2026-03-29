package pl.comp.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.comp.model.exceptions.NotCloneableException;
import pl.comp.model.exceptions.SerialReadException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.util.Arrays;

public class SudokuBoard implements java.io.Serializable, Cloneable {
    // deklaracja tablicy reprezentującej planszę
    private SudokuField[][] board;
    private transient SudokuSolver solver;

    private static final Logger logger = LoggerFactory.getLogger(SudokuBoard.class);

    //Konstruktor planszy. Ustawia jej rozmiar
    public SudokuBoard() {
        solver = new BacktrackingSudokuSolver();
        board = new SudokuField[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new SudokuField();
            }
        }
    }

    // Sprawdzenie, czy liczbę można postawić w board[row][column]
    public boolean checkBoard(int row, int column) {
        if (getField(row, column) == 0) {
            return true;
        }
        // Sprawdzenie w rzędach
        if (!getRow(row).verify()) {
            return false;
        }
        // Sprawdzenie w kolumnach
        if (!getColumn(column).verify()) {
            return false;
        }
        // Obliczanie numeru kwadratu
        int boxNumber = column / 3 + (3 * (row / 3));
        // Sprawdzenie w kwadracie 3x3
        if (!getBox(boxNumber).verify()) {
            return false;
        }
        // Jeśli nie znajdzie konfliktu zwraca true
        return true;
    }

    public int getField(int x, int y) {
        if (board[x][y] != null) {
            return board[x][y].getFieldValue();
        }
        return 0;
    }

    public SudokuField getFieldObject(int x, int y) {
        return board[x][y];
    }

    public void setField(int row, int column, int value) {
        if (board[row][column] == null) {
            board[row][column] = new SudokuField();
        }
        board[row][column].setFieldValue(value);
    }

    public SudokuRow getRow(int row) {
        return new SudokuRow(board, row);
    }

    public SudokuColumn getColumn(int column) {
        return new SudokuColumn(board, column);
    }

    public SudokuBox getBox(int i) {
        return new SudokuBox(board, i);
    }

    public void solveGame() {
        solver.solve(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("board", Arrays.deepToString(board)).toString();
    }

    @Override
    public  boolean equals(Object obj) {
        //jeśli porównujemy aktualny obiekt z aktualnym obiektem
        if (obj == this) {
            return true;
        }
        //jeśli porównywany obiekt jest nullem lub innej klasy
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        //append widzi, że tablica jest z obiektów, więc automatycznie wywołuje dla nich ich equals()
        return new EqualsBuilder().append(board, ((SudokuBoard) obj).board).isEquals();
    }

    @Override
    public int hashCode() {
        //działa analogicznie do equals()
        return new HashCodeBuilder(17, 37).append(board).toHashCode();
    }

    @Serial
    private void readObject(ObjectInputStream in) {
        try {
            in.defaultReadObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Could not read object - SudokuBoard", e);
            throw new SerialReadException("error.serialReadException", e);
        }
        this.solver = new BacktrackingSudokuSolver();
    }

    @Override
    public SudokuBoard clone() {
        try {
            SudokuBoard clone = (SudokuBoard) super.clone();
            clone.board = new SudokuField[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    clone.board[i][j] = this.board[i][j].clone();
                }
            }
            clone.solver = this.solver;
            return clone;
        } catch (CloneNotSupportedException e) {
            logger.error("Clone not supported - Sudoku Board.", e);
            throw new NotCloneableException("error.cloneException", e);
        }
    }
}
