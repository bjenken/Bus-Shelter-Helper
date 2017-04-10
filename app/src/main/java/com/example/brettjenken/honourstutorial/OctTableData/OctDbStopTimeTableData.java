package com.example.brettjenken.honourstutorial.OctTableData;

import android.provider.BaseColumns;

/**
 * Created by Brett on 2/19/2017.
 */

public abstract class OctDbStopTimeTableData implements BaseColumns {
    public static final String TRIP_ID = "trip_id";
    public static final String ARRIVAL_TIME = "arrival_time";
    public static final String DEPARTURE_TIME = "departure_time";
    public static final String STOP_ID = "stop_id";
    public static final String STOP_SEQUENCE = "stop_sequence";
    public static final String PICKUP_TYPE = "pickup_type";
    public static final String DROP_OFF_TYPE = "drop_off_type";
    public static final String TABLE_NAME= "stop_times";
    public static final String DATABASE_NAME = "oc_transpo_info";
}
