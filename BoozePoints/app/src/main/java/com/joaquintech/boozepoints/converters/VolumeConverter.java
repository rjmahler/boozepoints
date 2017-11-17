package com.joaquintech.boozepoints.converters;

/**
 * Created by rjmahler on 10/1/2017.
 */

public class VolumeConverter extends UnitConverter{
    public VolumeConverter() {
        super();
        _units.put("millilitres", 1.0);
        _units.put("litres", 1000.0);
        _units.put("us_fl_oz", 33.81);
    }
}
