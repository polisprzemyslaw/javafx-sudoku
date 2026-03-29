package pl.comp.view;

import javafx.util.converter.IntegerStringConverter;


public class CustomIntegerStringConverter extends IntegerStringConverter {
    @Override
    public Integer fromString(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        return super.fromString(value);
    }

    @Override
    public String toString(Integer value) {
        if (value == null || value == 0) {
            return "";
        }
        return super.toString(value);
    }
}
