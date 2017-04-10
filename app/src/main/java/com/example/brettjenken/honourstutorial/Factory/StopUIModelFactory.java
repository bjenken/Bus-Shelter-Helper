package com.example.brettjenken.honourstutorial.Factory;

import android.database.Cursor;
import android.text.TextUtils;

import com.example.brettjenken.honourstutorial.DBTable.StopTableData;
import com.example.brettjenken.honourstutorial.Stop.StopUIModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Brett on 2/18/2017.
 */

public class StopUIModelFactory {
    public StopUIModelFactory(){
    }

    public StopUIModel getStopUIModel(){
        return new StopUIModel();
    }

    public StopUIModel getStopUIModel(Cursor cursor){
        StopUIModel output = getStopUIModel();
        output.setId(cursor.getString(cursor.getColumnIndex(StopTableData.STOP_NUMBER)));
        output.setLocation(cursor.getString(cursor.getColumnIndex(StopTableData.LOCATION)));
        output.setRoutes(cursor.getString(cursor.getColumnIndex(StopTableData.ROUTES)));
        return output;
    }
    
}
