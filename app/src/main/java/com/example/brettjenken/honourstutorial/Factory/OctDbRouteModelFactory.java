package com.example.brettjenken.honourstutorial.Factory;

import android.database.Cursor;

import com.example.brettjenken.honourstutorial.OctDbModel.OctDbRouteModel;
import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbRouteTableData;


/**
 * Created by Brett on 2/18/2017.
 */

public class OctDbRouteModelFactory {
    public OctDbRouteModelFactory(){
    }

    public OctDbRouteModel getRouteOctDbModel(){
        return new OctDbRouteModel();
    }

    public OctDbRouteModel getRouteOctDbModel(Cursor cursor){
        OctDbRouteModel output = getRouteOctDbModel();
        output.setRouteId(cursor.getString(cursor.getColumnIndex(OctDbRouteTableData.ROUTE_ID)).replace("\"", ""));
        output.setRouteShortName(cursor.getString(cursor.getColumnIndex(OctDbRouteTableData.ROUTE_SHORT_NAME)).replace("\"", ""));
        return output;
    }

}
