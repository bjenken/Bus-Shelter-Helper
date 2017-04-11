package com.example.brettjenken.honourstutorial.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.brettjenken.honourstutorial.Dao.OctDbRouteDao;
import com.example.brettjenken.honourstutorial.Dao.OctDbStopDao;
import com.example.brettjenken.honourstutorial.Dao.OctDbStopTimeDao;
import com.example.brettjenken.honourstutorial.Factory.OctDbRouteModelFactory;
import com.example.brettjenken.honourstutorial.Factory.OctDbStopModelFactory;
import com.example.brettjenken.honourstutorial.Factory.OctDbTripModelFactory;
import com.example.brettjenken.honourstutorial.Factory.RouteUiModelFactory;
import com.example.brettjenken.honourstutorial.Factory.ServiceRouteModelFactory;
import com.example.brettjenken.honourstutorial.Factory.ServiceStopModelFactory;
import com.example.brettjenken.honourstutorial.Factory.StopUiModelFactory;
import com.example.brettjenken.honourstutorial.OctDbModel.OctDbRouteModel;
import com.example.brettjenken.honourstutorial.OctDbModel.OctDbStopModel;
import com.example.brettjenken.honourstutorial.OctDbModel.OctDbTripModel;
import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbStopTableData;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceRouteModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Brett on 2/18/2017.
 */

public class OctDbService {
    StopUiModelFactory stopUiModelFactory;
    OctDbStopDao octDbStopDao;
    OctDbStopTimeDao octDbStopTimeDao;
    OctDbStopModelFactory octDbStopModelFactory;
    OctDbRouteModelFactory octDbRouteModelFactory;
    OctDbRouteDao octDbRouteDao;
    OctDbTripModelFactory octDbTripModelFactory;
    RouteUiModelFactory routeUiModelFactory;
    ServiceStopModelFactory serviceStopModelFactory;
    ServiceRouteModelFactory serviceRouteModelFactory;
    ServiceCallback callback;
    public OctDbService(Context context, ServiceCallback callback) {
        this.octDbStopDao = new OctDbStopDao(context);
        this.stopUiModelFactory = new StopUiModelFactory();
        this.octDbStopModelFactory = new OctDbStopModelFactory();
        this.octDbRouteModelFactory = new OctDbRouteModelFactory();
        this.octDbRouteDao = new OctDbRouteDao(context);
        this.octDbStopTimeDao = new OctDbStopTimeDao(context);
        this.octDbTripModelFactory = new OctDbTripModelFactory();
        this.routeUiModelFactory = new RouteUiModelFactory();
        this.serviceStopModelFactory = new ServiceStopModelFactory();
        this.serviceRouteModelFactory = new ServiceRouteModelFactory();
        this.callback = callback;
    }

    public boolean checkForStop(SQLiteDatabase db, String stopCode){
        Cursor cursor = octDbStopDao.getStop(db, stopCode);
        boolean output = cursor.moveToFirst();
        return output;
    }

    public void getStopWithRoutes(SQLiteDatabase db, String stopCode){
        OctDbStopModel stop = octDbStopModelFactory.getStopOctDbModel(octDbStopDao.getStop(db, stopCode));
        callback.serviceSuccess(serviceStopModelFactory.getStopWithRoutes(stop, this.getRoutesForStop(db, stopCode)));
    }

    private List<ServiceRouteModel> getRoutesForStop(SQLiteDatabase db, String stopCode){
        List<ServiceRouteModel> output = new ArrayList<ServiceRouteModel>();
        List<OctDbTripModel> uniqueTrips = this.getUniqueTripsForStop(db, stopCode);
        for(OctDbTripModel trip: uniqueTrips){
            OctDbRouteModel route = octDbRouteModelFactory.getRouteOctDbModel(octDbRouteDao.getRouteFromRouteId(db, trip.getRouteId()));
            output.add(serviceRouteModelFactory.getRoutesForStop(trip, route));
        }
        return output;
    }

    private List<String> getStopIdsForStop(SQLiteDatabase db, String stopCode){
        List<String> stopIds = new ArrayList<String>();
        Cursor cursorStop = octDbStopDao.getStop(db, stopCode);
        if (cursorStop != null) {
            while (cursorStop.moveToNext()) {
                stopIds.add(cursorStop.getString(cursorStop.getColumnIndex(OctDbStopTableData.STOP_ID)));
            }
        }
        return stopIds;
    }
    private List<OctDbTripModel> getUniqueTripsForStop(SQLiteDatabase db, String stopCode){
        Cursor cursor = octDbStopTimeDao.getUniqueTripsForStop(db, this.getStopIdsForStop(db, stopCode));
        List<OctDbTripModel> output = new ArrayList<OctDbTripModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                output.add(octDbTripModelFactory.getTripOctDbModel(cursor));
            }
        }
        return output;
    }

    public SQLiteDatabase getReadableDatabase(){
        return octDbStopDao.getReadableDatabase();
    }

}
