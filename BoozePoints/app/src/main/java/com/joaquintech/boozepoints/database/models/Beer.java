package com.joaquintech.boozepoints.database.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by rjmahler on 9/4/2017.
 */
@IgnoreExtraProperties
public class Beer {

    public String uid;
    public String name;
    public String searchable_name;
    public String type;
    public String place;


    public float price;
    public float abv;
    public String location;
    public int vol_milliliters;
    public int points;

    public Beer() {
        // Default constructor required for calls to DataSnapshot.getValue(Beer.class)
    }
    public Beer(String uid, String name, String location, float price, float abv, int vol_milliliters, int points) {
        this.uid = uid;
        this.name = name;
        this.location = location;
        this.price = price;
        this.abv = abv;
        this.vol_milliliters = vol_milliliters;
        this.points = points;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("location", location);
        result.put("price", price);
        result.put("abv", abv);
        result.put("vol_milliliters",vol_milliliters);
        result.put("points", points);
        result.put("type", type);
        result.put("place", place);
        result.put("searchable_name", searchable_name);
        return result;
    }
}
