package pl.comp.bundles;

import java.util.ListResourceBundle;

public class Authors extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
                { "authors", new String[] {"Przemyslaw Polis", "John Wolniewski"} },
                { "university", "Lodz University of Technology" }
        };
    }
}
