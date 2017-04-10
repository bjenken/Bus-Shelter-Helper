package com.example.brettjenken.honourstutorial.Factory;

import android.database.Cursor;

import com.example.brettjenken.honourstutorial.DBTable.AppDbStopTableData;
import com.example.brettjenken.honourstutorial.Ui.Stop.StopUiModel;


/**
 * Created by Brett on 2/18/2017.
 */

public class StopUiModelFactory {
    public StopUiModelFactory(){
    }

    public StopUiModel getStopUIModel(){
        return new StopUiModel();
    }

    public StopUiModel getStopUIModel(Cursor cursor){
        StopUiModel output = getStopUIModel();
        output.setId(cursor.getString(cursor.getColumnIndex(AppDbStopTableData.STOP_NUMBER)));
        output.setLocation(cursor.getString(cursor.getColumnIndex(AppDbStopTableData.LOCATION)));
        output.setRoutes(cursor.getString(cursor.getColumnIndex(AppDbStopTableData.ROUTES)));
        return output;
    }

}
