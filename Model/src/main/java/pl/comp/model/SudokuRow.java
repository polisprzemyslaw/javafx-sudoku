package pl.comp.model;

public class SudokuRow extends SudokuStruct {
    public SudokuRow(SudokuField[][] board, int index) {
        //wywołanie konstruktora klasy nadrzędnej
        super(index);
        //Wypełnienie tablicy pól polami z planszy
        for (int i = 0; i < 9; i++) {
            fields[i] = board[index][i];
        }
    }

    @Override
    public SudokuRow clone() {
        return (SudokuRow) super.clone();
    }
}