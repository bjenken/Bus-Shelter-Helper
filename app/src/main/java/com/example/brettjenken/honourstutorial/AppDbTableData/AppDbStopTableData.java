package com.example.brettjenken.honourstutorial.AppDbTableData;

import android.provider.BaseColumns;

/**
 * Created by Brett on 2/11/2017.
 */

public abstract class AppDbStopTableData implements BaseColumns{
    public static final String STOP_NUMBER = "stop_number";
    public static final String LOCATION = "location";
    public static final String ROUTES = "routes";
    public static final String LAST_ACCESSED = "last_accessed";
    public static final String TABLE_NAME = "stop_info";
    public static final String DATABASE_NAME = "bus_tracker_info";
}
