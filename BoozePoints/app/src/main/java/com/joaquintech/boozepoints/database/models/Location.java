package com.joaquintech.boozepoints.database.models;

import com.joaquintech.boozepoints.enums.States;

/**
 * Created by rjmahler on 10/4/2017.
 */

public class Location {

    public static boolean validateState(String location) {
        String[] cityState = location.trim().split(",");
        //only validating state for now
        if(cityState.length != 2 || States.parse(cityState[1]) == null) {
            return false;
        }
        return true;
    }
}
