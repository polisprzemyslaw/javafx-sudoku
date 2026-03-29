package pl.comp.bundles;

import java.util.ListResourceBundle;

public class Authors_pl extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
                { "authors", new String[] {"Przemysław Polis", "Jan Wolniewski"} },
                { "university", "Politechnika Łódzka" }
        };
    }
}
