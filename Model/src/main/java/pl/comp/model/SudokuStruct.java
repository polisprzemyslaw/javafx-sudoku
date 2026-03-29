package pl.comp.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.comp.model.exceptions.NotCloneableException;

import java.util.Arrays;

abstract class SudokuStruct implements Cloneable {
    //Pola w danej części planszy Sudoku
    protected SudokuField[] fields;
    //Indeks części planszy Sudoku
    protected int index;
    private static final Logger logger = LoggerFactory.getLogger(SudokuStruct.class);

    //Konstruktor części planszy Sudoku
    SudokuStruct(int index) {
        this.index = index;
        fields = new SudokuField[9];
    }

    //Sprawdzenie fragmentu planszy Sudoku
    public boolean verify() {
        //tablica wystąpień cyfr we fragmencie
        boolean[] numberAppeared = new boolean[9];
        for (int i = 0; i < 9; i++) {
            numberAppeared[i] = false;
        }
        //sprawdzenie potencjalnych powtórzeń
        for (int i = 0; i < 9; i++) {
            if (fields[i] != null && fields[i].getFieldValue() != 0 &&  numberAppeared[fields[i].getFieldValue() - 1]) {
                return false;
            } else if (fields[i] != null && fields[i].getFieldValue() != 0) {
                numberAppeared[fields[i].getFieldValue() - 1] = true;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("fields", Arrays.deepToString(fields)).toString();
    }

    @Override
    public  boolean equals(Object obj) {
        //jeśli porównujemy aktualny obiekt z aktualnym obiektem
        if (obj == this) {
            return true;
        }
        //jeśli porównywany obiekt jest nullem albo jest inną klasą niż bieżąca (klasy dziedziczące też się różnią)
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        //append widzi, że tablica jest z obiektów, więc automatycznie wywołuje dla nich ich equals()
        return new EqualsBuilder().append(fields, ((SudokuStruct) obj).fields).isEquals();
    }

    @Override
    public int hashCode() {
        //działa analogicznie do equals()
        return new HashCodeBuilder(13, 41).append(fields).toHashCode();
    }

    @Override
    public SudokuStruct clone() {
        try {
            SudokuStruct clone = (SudokuStruct) super.clone();
            clone.fields = new SudokuField[9];
            for (int i = 0; i < 9; i++) {
                clone.fields[i] = this.fields[i].clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            logger.error("Clone not supported - Sudoku Struct.", e);
            throw new NotCloneableException("error.cloneException", e);
        }
    }
}
