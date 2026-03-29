package pl.comp.model;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import pl.comp.model.exceptions.NotCloneableException;
import pl.comp.model.exceptions.NullFieldException;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuBoardTest {
    @RepeatedTest(20)
    public void numbersCorrectTest() {
        SudokuBoard testBoard = new SudokuBoard();
        testBoard.solveGame();
        boolean[] numberAppeared = new boolean[9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                System.out.print(testBoard.getField(i,j) + " ");
            System.out.println();
        }
        System.out.println();
        //w kwadracie 3x3
        for (int i = 0; i < 3; i++) { //"wiersz kwadratu"
            for (int j = 0; j < 3; j++) { //"kolumna kwadratu"
                //zerowanie tablicy użytych liczb dla kolejnego kwadratu 3x3
                for (int k = 0; k < 9; k++) {
                    numberAppeared[k] = false;
                }
                //ustawienie indeksów lewego górnego rogu kwadratu 3x3
                int boardX = i*3;
                int boardY = j*3;
                //Sprawdzenie dla każdego kwadratu 3x3, czy liczba się nie powtarza
                for (int l = 0; l < 3; l++) { //"wiersz małego kwadratu"
                    for (int m = 0; m < 3; m++) { //"kolumna małego kwadratu"
                        System.out.print(testBoard.getField(boardX + l, boardY + m) + "  ");
                        assertFalse(numberAppeared[testBoard.getField(boardX+l,boardY+m) - 1]);
                        numberAppeared[testBoard.getField(boardX+l,boardY+m) - 1] = true;
                    }
                }
                //Sprawdzenie, czy wszystkie liczby zostały użyte
                for (int k = 0; k < 9; k++) {
                    assertTrue(numberAppeared[k]);
                }
                System.out.println();
            }
        }
        System.out.println();
        //w wierszach
        for (int i = 0; i < 9; i++) { //wiersz
            for (int k = 0; k < 9; k++) {
                numberAppeared[k] = false;
            }
            //Sprawdzenie dla każdego wiersza, czy liczba się nie powtarza
            for (int j = 0; j < 9; j++) { //komórka
                System.out.print(testBoard.getField(i,j) + "  ");
                assertFalse(numberAppeared[testBoard.getField(i,j) - 1]);
                numberAppeared[testBoard.getField(i,j) - 1] = true;
            }
            System.out.println();
        }
        System.out.println();
        //w kolumnach
        for (int i = 0; i < 9; i++) { //wiersz
            for (int k = 0; k < 9; k++) {
                numberAppeared[k] = false;
            }
            //Sprawdzenie dla każdej kolumny, czy liczba się nie powtarza
            for (int j = 0; j < 9; j++) { //kolumna
                System.out.print(testBoard.getField(j,i) + "  ");
                assertFalse(numberAppeared[testBoard.getField(j,i) - 1]);
                numberAppeared[testBoard.getField(j,i) - 1] = true;
            }
            System.out.println();
        }
    }

    @RepeatedTest(10)
    public void randomnessTest() {//test różności tablic
        SudokuBoard testBoard1 = new SudokuBoard();
        SudokuBoard testBoard2 = new SudokuBoard();
        testBoard1.solveGame();
        testBoard2.solveGame();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                System.out.print(testBoard1.getField(i,j) + " ");
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                System.out.print(testBoard2.getField(i,j) + " ");
            System.out.println();
        }
        System.out.println();

        boolean isDifferent = false;
        //Sprawdza, czy między dwoma planszami jest różnica
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (testBoard1.getField(i,j) != testBoard2.getField(i,j)) {
                    isDifferent = true;
                    break;
                }
            }
            if (isDifferent) break;
        }
        assertTrue(isDifferent);
    }

    /*
    @Test
    public void listenerTest() {
        SudokuBoard testBoard = new SudokuBoard();
        testBoard.setField(0, 0, 5);
        testBoard.setField(0, 1, 5);
        assertEquals(0, testBoard.getField(0, 1));
        testBoard.setField(5, 5, 9);
        testBoard.setField(5, 4, 6);
        testBoard.setField(5, 4, 9);
        assertEquals(6, testBoard.getField(5, 4));

        SudokuField testField = new SudokuField();
        testField.setFieldValue(3);
        testField.addFieldValueListener(evt -> {
            testField.setFieldValue(5);
        });

        testField.setFieldValue(4);
        assertEquals(5, testField.getFieldValue());
        testField.removeFieldValueListener(evt -> {
            testField.setFieldValue(5);
        });
        testField.setFieldValue(7);
        assertEquals(5, testField.getFieldValue());
    }
     */

    @Test
    public void BoardHashCodeEqualsTest() {
        SudokuBoard testBoard1 = new SudokuBoard();
        SudokuBoard testBoard2 = testBoard1.clone();

        // sprawdzenie dla null
        assertNotEquals(null, testBoard1);

        // sprawdzenie dla tego samego obiektu
        assertEquals(testBoard1.hashCode(), testBoard1.hashCode());
        assertEquals(testBoard1, testBoard1);

        // sprawdzenie dla innej klasy
        assertNotEquals(new SudokuField(), testBoard1);


        // sprawdzenie równości przy zerowej tablicy
        assertEquals(testBoard1.hashCode(), testBoard2.hashCode());
        assertEquals(testBoard1, testBoard2);

        testBoard1.setField(5, 5, 4);
        testBoard2.setField(5, 5, 3);

        // sprawdzenie 2 różnych tablic
        assertNotEquals(testBoard1, testBoard2);

        testBoard2.setField(5, 5, 4);

        // sprawdzenie po zmianie na te same wartości w obu tablicach
        assertEquals(testBoard1.hashCode(), testBoard2.hashCode());
        assertEquals(testBoard1, testBoard2);

        // wypisanie tablicy
        System.out.println(testBoard1);
    }

    @Test
    public void StructHashCodeEqualsTest() {
        SudokuBoard testBoard1 = new SudokuBoard();


        testBoard1.setField(2, 3, 5);

        SudokuBoard testBoard2 = testBoard1.clone();
        // sprawdzenie czy Row w 2 tablicach są takie same
        assertEquals(testBoard1.getRow(3).hashCode(), testBoard2.getRow(3).hashCode());
        assertEquals(testBoard1.getRow(3), testBoard2.getRow(3));

        // Sprawdzenie czy wykrywa różnicę w wartościach Row
        testBoard1.setField(2, 3, 1);
        assertEquals(testBoard1.getRow(3), testBoard2.getRow(3));

        // Sprawdzenie czy równe Row w jednej tablicy
        SudokuBoard testBoard3 = new SudokuBoard();
        assertEquals(testBoard3.getRow(2).hashCode(), testBoard3.getRow(3).hashCode());
        assertEquals(testBoard3.getRow(2), testBoard3.getRow(3));

        // sprawdzenie czy wykrywa różnice w wartościach Row w jednej tablicy
        testBoard3.setField(5, 4, 3);
        testBoard3.setField(6, 4, 4);
        assertNotEquals(testBoard3.getRow(6), testBoard3.getRow(5));

        // Sprawdzenie czy rozróżnia pochodne SudokuStruct
        assertNotEquals(testBoard1.getRow(7), testBoard1.getColumn(7));

        // sprawdzenie dla tego samego obiektu
        SudokuRow row = testBoard3.getRow(5);
        assertEquals(row, row);

        // sprawdzenie dla null
        assertNotEquals(null, testBoard3.getRow(5));

        // wypisanie row
        System.out.println(row);

        // Nie potrzeba testów dla Column i Box bo ich metody equals() i hashCode() działają tak samo
    }

    @Test
    public void FieldHashCodeEqualsTest() {
        SudokuField f1 = new SudokuField();

        // sprawdzenie dla tego samego obiektu
        assertEquals(f1, f1);
        assertEquals(f1.hashCode(), f1.hashCode());

        // sprawdzenie dla null
        assertNotEquals(null, f1);

        //sprawdzenie dla innej klasy
        assertNotEquals(new String(), f1);

        f1.setFieldValue(5);
        SudokuField f2 = f1.clone();

        // sprawdzenie czy równe są pola
        assertEquals(f1.hashCode(), f2.hashCode());
        assertEquals(f1, f2);

        f2.setFieldValue(3);

        // sprawdzenie czy wykrywa różnice w polach
        assertNotEquals(f1, f2);

        // wypisanie pola
        System.out.println(f1);
    }

    @Test
    public void CloneableBoardTest() throws NotCloneableException {
        SudokuBoard testBoard1 = new SudokuBoard();
        testBoard1.solveGame();
        SudokuBoard testBoard2 = testBoard1.clone();

        assertNotSame(testBoard1, testBoard2);
        assertEquals(testBoard1, testBoard2);

    }

    /*
    @Test
    public void CloneableListenerTest() throws Exception {
        SudokuBoard testBoard1 = new SudokuBoard();
        SudokuBoard testBoard2 = testBoard1.clone();
        testBoard2.setField(1,5, 5);
        testBoard2.setField(2,5, 5);
        assertEquals(0, testBoard2.getField(2,5));
    }
     */

    @Test
    public void CloneableSolverTest() throws NotCloneableException {
        SudokuBoard testBoard1 = new SudokuBoard();
        SudokuBoard testBoard2 = testBoard1.clone();

        testBoard2.solveGame();
        assertNotEquals(0, testBoard2.getField(4,5));
    }

    @Test
    public void CloneableStructTest() throws NotCloneableException {
        SudokuBoard testBoard = new SudokuBoard();
        testBoard.solveGame();
        SudokuRow row1 = testBoard.getRow(1);
        SudokuRow row2 = row1.clone();

        assertNotSame(row1, row2);
        assertEquals(row1, row2);

        SudokuColumn column1 = testBoard.getColumn(3);
        SudokuColumn column2 = column1.clone();

        assertNotSame(column1, column2);
        assertEquals(column1, column2);

        SudokuBox box1 = testBoard.getBox(5);
        SudokuBox box2 = box1.clone();

        assertNotSame(box1, box2);
        assertEquals(box1, box2);
    }

    @Test
    public void CloneableFieldTest() throws NotCloneableException {
        SudokuField field1 = new SudokuField();
        field1.setFieldValue(3);
        SudokuField field2 = field1.clone();

        assertNotSame(field1, field2);
        assertEquals(field1, field2);
    }

    @Test
    public void ComparableFieldTest() throws NullFieldException {
        SudokuField field1 = new SudokuField();
        field1.setFieldValue(2);
        SudokuField field2 = field1.clone();
        field2.setFieldValue(5);

        assertTrue(field1.compareTo(field2) < 0);
        assertTrue(field2.compareTo(field1) > 0);
        field2.setFieldValue(2);
        assertEquals(0, field1.compareTo(field2));

        assertThrows(NullFieldException.class, () -> field1.compareTo(null));
    }
}


