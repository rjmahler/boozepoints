package com.joaquintech.boozepoints.converters;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by rjmahler on 10/1/2017.
 */

public class UnitConverter {
    protected HashMap<String, Double> _units;

    public UnitConverter()
    {
        _units = new HashMap<String, Double>();
    }

    public double convert(String from, String to, double value)
    {
        return value * _units.get(from) / _units.get(to);
    }

    protected Set<String> availableUnits()
    {
        return _units.keySet();
    }
}
