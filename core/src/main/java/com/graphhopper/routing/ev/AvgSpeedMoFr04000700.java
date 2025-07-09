package com.graphhopper.routing.ev;

import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.DecimalEncodedValueImpl;

/**
 * This EncodedValue stores average speed values for Monday-Friday 04:00-07:00 time period in km/h.
 */
public class AvgSpeedMoFr04000700 {
    public static final String KEY = "avgspeed_mo_fr_0700_0900_forward";

    /**
     * Default speed value when no data is available for this time period
     */
    public static final double DEFAULT_SPEED = 50;
    
    /**
     * The speed value used for road sections without known speed data for this time period
     */
    public static final double SPEED_MISSING = Double.POSITIVE_INFINITY;

    public static DecimalEncodedValue create() {
        // 7 bits allows values up to 127 * 2 = 254 km/h
        // factor of 2 allows 0.5 km/h precision
        // storing directional values (true for directional parameter)
        return new DecimalEncodedValueImpl(KEY, 7, 0, 2, false, true, true);
    }
}
