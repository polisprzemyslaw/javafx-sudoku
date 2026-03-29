package pl.comp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BacktrackingSudokuSolver implements SudokuSolver {

    //Wywołanie algorytmu
    @Override
    public void solve(SudokuBoard board) {
        solveRec(board,0, 0);
    }

    // Wypełnienie planszy Sudoku rekurencyjnie
    private boolean solveRec(SudokuBoard board, int row, int column) {

        // Przypadek bazowy, koniec planszy
        if (row == 8 && column == 9) {
            return true;
        }

        // Przejście na kolejny wiersz, gdy dotrze do ostatniej kolumny
        if (column == 9) {
            row++;
            column = 0;
        }

        // Przejście na kolejne pole, jeśli jest już wypełnione
        if (board.getField(row, column) != 0) {
            return solveRec(board, row, column + 1);
        }
        //Lista liczb
        List<Integer> numbers = new ArrayList<>();
        //Wypełnienie listy liczbami od 1 do 9
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        //Losowanie kolejności liczb w liście
        Collections.shuffle(numbers);
        // Pętla sprawdzająca poprawność liczb od 1 do 9 w losowej kolejności
        for (int i: numbers) {

            // Jeśli liczba pasuje, wpisuje ją w pole
            board.setField(row, column, i);
            //sprawdzenie poprawności pola i wywołanie funkcji dla kolejnego pola
            if (board.checkBoard(row, column) && solveRec(board, row, column + 1)) {
                return true;
            }
            // jeśli nie uda się znaleźć rozwiązania dla następnych pól,
            // cofa wpisanie liczby i sprawdza kolejne liczby
            board.setField(row, column, 0);
        }
        //Zwraca false, jeśli nie da się wypełnić pola żadną liczbą
        return false;
    }
}
