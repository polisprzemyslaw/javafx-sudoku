package pl.comp.model;

public class SudokuBox extends SudokuStruct {
    public SudokuBox(SudokuField[][] board, int index) {
        //Wywołanie konstruktora klasy nadrzędnej
        super(index);
        //Przeliczenie numeru kwadratu na indeksy w tablicy
        int row = index / 3 * 3;
        int col = index % 3 * 3;
        int k = 0;
        //Wypełnienie tablicy pól polami z planszy
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                fields[k] = board[row + i][col + j];
                k++;
            }
        }
    }

    @Override
    public SudokuBox clone() {
        return (SudokuBox) super.clone();
    }
}
