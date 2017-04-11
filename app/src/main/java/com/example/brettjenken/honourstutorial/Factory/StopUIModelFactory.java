package com.example.brettjenken.honourstutorial.Factory;

import android.database.Cursor;
import android.text.TextUtils;

import com.example.brettjenken.honourstutorial.AppDbTableData.AppDbStopTableData;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceRouteModel;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceStopModel;
import com.example.brettjenken.honourstutorial.Ui.Stop.StopUiModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    public StopUiModel getStopUIModel(ServiceStopModel input){
        StopUiModel output = getStopUIModel();
        output.setId(input.getStopNo());
        output.setLocation(input.getStopDescription());

        Set<Integer> routeNums = new HashSet<Integer>();
        for(ServiceRouteModel r: input.getRoutes()){
            routeNums.add(r.getRouteNo());
        }
        List sortedRoutes = new ArrayList(routeNums);
        Collections.sort(sortedRoutes);
        String routes = TextUtils.join(", ", sortedRoutes);

        output.setRoutes(routes);
        return output;
    }

}
