package com.joaquintech.boozepoints.enums;

/**
 * Created by rjmahler on 11/17/2017.
 */

public enum BeerStyles {
    AMBER("Amber"),
    BARLEY("Barley"),
    BLONDE("Blonde"),
    BROWN("Brown"),
    CREAM("Cream"),
    DUNKEL("Dunkel"),
    IPA("IPA"),
    LIGHT("Light"),
    MILD("Mild"),
    OLD("Old"),
    PALE_ALE("Pale Ale"),
    RED("Red"),
    OTHER("Other");

    String name;

    BeerStyles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
