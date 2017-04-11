package com.example.brettjenken.honourstutorial.Factory;

import android.database.Cursor;

import com.example.brettjenken.honourstutorial.OctDbModel.OctDbTripModel;
import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbTripTableData;


/**
 * Created by Brett on 2/18/2017.
 */

public class OctDbTripModelFactory {
    public OctDbTripModelFactory(){
    }

    public OctDbTripModel getTripOctDbModel(){
        return new OctDbTripModel();
    }

    public OctDbTripModel getTripOctDbModel(Cursor cursor){
        OctDbTripModel output = getTripOctDbModel();
        output.setRouteId(cursor.getString(cursor.getColumnIndex(OctDbTripTableData.ROUTE_ID)).replace("\"", ""));
        output.setServiceId(cursor.getString(cursor.getColumnIndex(OctDbTripTableData.SERVICE_ID)).replace("\"", ""));
        output.setTripId(cursor.getString(cursor.getColumnIndex(OctDbTripTableData.TRIP_ID)).replace("\"", ""));
        output.setTripHeadsign(cursor.getString(cursor.getColumnIndex(OctDbTripTableData.TRIP_HEADSIGN)).replace("\"", ""));
        output.setDirection(cursor.getString(cursor.getColumnIndex(OctDbTripTableData.DIRECTION_ID)).replace("\"", ""));
        return output;
    }

}
