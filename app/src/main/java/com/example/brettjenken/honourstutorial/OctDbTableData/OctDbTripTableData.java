package com.example.brettjenken.honourstutorial.OctDbTableData;

import android.provider.BaseColumns;

/**
 * Created by Brett on 2/19/2017.
 */

public abstract class OctDbTripTableData implements BaseColumns {
    public static final String ROUTE_ID = "route_id";
    public static final String SERVICE_ID = "service_id";
    public static final String TRIP_ID = "trip_id";
    public static final String TRIP_HEADSIGN = "trip_headsign";
    public static final String DIRECTION_ID= "direction_id";
    public static final String BLOCK_ID= "block_id";
    public static final String TABLE_NAME= "trips";
    public static final String DATABASE_NAME = "oc_transpo_info";
}
