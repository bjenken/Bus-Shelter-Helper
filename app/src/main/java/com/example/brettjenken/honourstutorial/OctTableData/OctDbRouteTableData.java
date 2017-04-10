package com.example.brettjenken.honourstutorial.OctTableData;

import android.provider.BaseColumns;

/**
 * Created by Brett on 2/19/2017.
 */

public abstract class OctDbRouteTableData implements BaseColumns {
    public static final String ROUTE_ID = "route_id";
    public static final String ROUTE_SHORT_NAME = "route_short_name";
    public static final String ROUTE_LONG_NAME = "route_long_name";
    public static final String ROUTE_DESC = "route_desc";
    public static final String ROUTE_TYPE= "route_type";
    public static final String ROUTE_URL= "route_url";
    public static final String TABLE_NAME= "routes";
    public static final String DATABASE_NAME = "oc_transpo_info";
}
