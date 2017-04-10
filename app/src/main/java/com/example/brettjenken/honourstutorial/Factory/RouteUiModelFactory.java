package com.example.brettjenken.honourstutorial.Factory;

import android.database.Cursor;

import com.example.brettjenken.honourstutorial.DBTable.AppDbRouteTableData;
import com.example.brettjenken.honourstutorial.Ui.Route.RouteUiModel;


/**
 * Created by Brett on 2/18/2017.
 */

public class RouteUiModelFactory {
    public RouteUiModelFactory(){
    }

    public RouteUiModel getRouteUIModel(){
        return new RouteUiModel();
    }

    public RouteUiModel getRouteUIModel(Cursor cursor){
        RouteUiModel output = this.getRouteUIModel();
        output.setNumber(cursor.getString(cursor.getColumnIndex(AppDbRouteTableData.ROUTE_NUMBER)));
        output.setDirection(cursor.getString(cursor.getColumnIndex(AppDbRouteTableData.DIRECTION)));
        return output;
    }
}
