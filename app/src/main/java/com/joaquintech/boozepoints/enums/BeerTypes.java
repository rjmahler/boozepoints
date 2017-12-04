package com.joaquintech.boozepoints.enums;

/**
 * Created by rjmahler on 11/17/2017.
 */

public enum BeerTypes {
    PALE_ALE("Ale"),
    LAGER("Lager"),
    PILSNER("Pilsner"),
    WHEAT_BEER("Wheat Beer"),
    GOLDEN_ALE("Golden Ale"),
    DARK_ALE("Dark Ale"),
    PORTER("Porter"),
    STOUT("Stout"),
    IMPERIAL_STOUT("Imperial Stout"),
    OTHER("Other");

    String name;

    BeerTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}