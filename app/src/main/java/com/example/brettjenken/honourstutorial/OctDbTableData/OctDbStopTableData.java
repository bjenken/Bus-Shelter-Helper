package com.example.brettjenken.honourstutorial.OctDbTableData;

import android.provider.BaseColumns;

/**
 * Created by Brett on 2/19/2017.
 */

public abstract class OctDbStopTableData implements BaseColumns {
    public static final String STOP_ID = "stop_id";
    public static final String STOP_CODE = "stop_code";
    public static final String STOP_NAME = "stop_name";
    public static final String STOP_DESC = "stop_desc";
    public static final String STOP_LAT = "stop_lat";
    public static final String STOP_LON = "stop_lon";
    public static final String ZONE_ID = "zone_id";
    public static final String STOP_URL = "stop_url";
    public static final String LOCATION_TYPE = "location_type";
    public static final String TABLE_NAME= "stops";
    public static final String DATABASE_NAME = "oc_transpo_info";
}
