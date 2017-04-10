package com.example.brettjenken.honourstutorial.DBTable;

import android.provider.BaseColumns;

/**
 * Created by Brett on 2/11/2017.
 */

public abstract class AppDbRouteTableData implements BaseColumns{
    public static final String STOP_NUMBER = "stop_number";
    public static final String ROUTE_NUMBER = "route_number";
    public static final String DIRECTION = "direction";
    public static final String TABLE_NAME= "route_info";
    public static final String DATABASE_NAME = "bus_tracker_info";
}
