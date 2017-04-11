package com.example.brettjenken.honourstutorial.Factory;

import android.database.Cursor;

import com.example.brettjenken.honourstutorial.AppDbTableData.AppDbRouteTableData;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceRouteModel;
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

    public RouteUiModel getRouteUIModel(ServiceRouteModel input){
        RouteUiModel output = this.getRouteUIModel();
        output.setNumber(Integer.toString(input.getRouteNo()));
        output.setDirection(input.getDirection());
        return output;
    }
}
