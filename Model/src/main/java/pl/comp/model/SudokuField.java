package pl.comp.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.comp.model.exceptions.InvalidValueException;
import pl.comp.model.exceptions.NotCloneableException;
import pl.comp.model.exceptions.NullFieldException;

public class SudokuField implements java.io.Serializable, Cloneable, Comparable<SudokuField> {
    private int fieldValue;

    private static final Logger logger = LoggerFactory.getLogger(SudokuField.class);

    public int getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(int newValue) {
        if (newValue >= 0 && newValue <= 9) {
            this.fieldValue = newValue;
        } else {
            logger.error("Invalid value: {}", newValue);
            throw new InvalidValueException("error.invalidValue", newValue);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("fieldValue", fieldValue).toString();
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
        return new EqualsBuilder().append(fieldValue, ((SudokuField) obj).getFieldValue()).isEquals();
    }

    @Override
    public int hashCode() {
        //działa analogicznie do equals()
        return new HashCodeBuilder(7, 19).append(fieldValue).toHashCode();
    }

    @Override
    public SudokuField clone() {
        try {
            return (SudokuField) super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error("Clone not supported - SudokuField", e);
            throw new NotCloneableException("error.cloneException", e);
        }
    }

    @Override
    public int compareTo(SudokuField other) {
        if (other == null) {
            logger.error("Comparing to null - SudokuField");
            throw new NullFieldException("error.nullField");
        }
        return this.fieldValue - other.fieldValue;
    }
}
