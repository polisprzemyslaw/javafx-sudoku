package pl.comp.model;

public class SudokuColumn extends SudokuStruct {
    public SudokuColumn(SudokuField[][] board, int index) {
        //Wywołanie konstruktora klasy nadrzędnej
        super(index);
        //Wypełnienie tablicy pól polami z planszy
        for (int i = 0; i < 9; i++) {
            fields[i] = board[i][index];
        }
    }

    @Override
    public SudokuColumn clone() {
        return (SudokuColumn) super.clone();
    }
}
