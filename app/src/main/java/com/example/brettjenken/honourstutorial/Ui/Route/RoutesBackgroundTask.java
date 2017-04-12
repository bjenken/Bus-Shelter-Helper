package com.example.brettjenken.honourstutorial.Ui.Route;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.brettjenken.honourstutorial.Factory.RouteUiModelFactory;
import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Service.AppDbRouteService;
import com.example.brettjenken.honourstutorial.Service.AppDbStopService;
import com.example.brettjenken.honourstutorial.Service.OctDbService;
import com.example.brettjenken.honourstutorial.Service.ServiceCallback;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceStopModel;
import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;
import com.example.brettjenken.honourstutorial.Ui.UiUtils;

import java.util.Collections;

/**
 * Created by Brett on 11/24/2016.
 */
public class RoutesBackgroundTask extends AsyncTask<String, RouteUiModel, String>{
    Context context;
    RouteAdapter routeAdapter;
    Activity activity;
    ListView listView;
    UiBackgroundTaskCallback callback;
    AppDbRouteService appDbRouteService;
    AppDbStopService appDbStopService;
    RouteUiModelFactory routeUiModelFactory;
    OctDbService octDbService;
    ServiceStopModel serviceStopModel;
    RoutesBackgroundTask(UiBackgroundTaskCallback callback, Context context, ServiceCallback serviceCallback, ServiceStopModel serviceStopModel) {
        this.callback = callback;
        this.context = context;
        this.activity = (Activity)context;
        this.appDbRouteService = new AppDbRouteService(context);
        this.routeUiModelFactory = new RouteUiModelFactory();
        this.octDbService = new OctDbService(context, serviceCallback);
        this.appDbStopService = new AppDbStopService(context);
        this.serviceStopModel = serviceStopModel;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        UiUtils.RouteBackgroundTaskInputValues inputCase =
                UiUtils.RouteBackgroundTaskInputValues.valueOf(params[0]);
        switch(inputCase){
            case GET_ROUTES_FROM_APP_DB:
                return this.getRoutesFromAppDb(params[1]);
            case GET_ROUTES_FROM_STOP_OBJECT:
                return this.getRoutesFromStopObject();
            case CHECK_APP_DB_FOR_STOP:
                return this.checkAppDBForStop(params[1]);
            case UPDATE_STOP_IN_APP_DB:
                return this.updateStopInAppDb(params[1]);
            case GET_STOP_FROM_OC_DB:
                return this.getStopFromOcDb(params[1]);
            case ADD_STOP_TO_APP_DB:
                return this.addStopToAppDb();
        }
        return null;
    }

    private String checkAppDBForStop(String stopId) {
        SQLiteDatabase db = appDbStopService.getReadableDatabase();
        boolean check = appDbStopService.stopInTable(db, stopId);
        if (check){
            callback.backGroundTaskSuccess(UiUtils.RouteBackgroundTaskReturnValues.STOP_FOUND.name());
        } else {
            callback.backGroundTaskSuccess(UiUtils.RouteBackgroundTaskReturnValues.STOP_NOT_FOUND.name());
        }
        return UiUtils.RouteBackgroundTaskReturnValues.APP_DB_CHECKED_FOR_STOP.name();
    }

    private String getRoutesFromAppDb(String stopNum){
        listView = (ListView) activity.findViewById(R.id.routesListListView);
        SQLiteDatabase db = appDbRouteService.getReadableDatabase();
        Cursor cursor = appDbRouteService.getAllEntriesForStop(db, stopNum);
        while(cursor.moveToNext()){
            publishProgress(routeUiModelFactory.getRouteUIModel(cursor));
        }
        routeAdapter = new RouteAdapter(context, R.layout.display_route_row_layout);

        return UiUtils.RouteBackgroundTaskReturnValues.ROUTES_RETRIEVED_FROM_APP_DB.name();
    }

    private String updateStopInAppDb(String stopNum){
        SQLiteDatabase db = appDbStopService.getWritableDatabase();
        appDbStopService.updateAccessedTime(db, stopNum);
        return UiUtils.RouteBackgroundTaskReturnValues.STOP_UPDATED_IN_APP_DB.name();
    }

    private String getStopFromOcDb(String stopId){
        octDbService.getStopWithRoutes(octDbService.getReadableDatabase(), stopId);
        return UiUtils.RouteBackgroundTaskReturnValues.STOP_RETRIEVED_FROM_OC_DB.name();
    }

    private String addStopToAppDb(){
        SQLiteDatabase db = appDbStopService.getWritableDatabase();
        //delete any row that already exists with that stop number
        appDbStopService.deleteRow(db, serviceStopModel.getStopNo());
        appDbStopService.insertRow(db, serviceStopModel);
        return UiUtils.RouteBackgroundTaskReturnValues.STOP_ADDED_TO_APP_DB.name();
    }

    private String getRoutesFromStopObject(){
        listView = (ListView) activity.findViewById(R.id.routesListListView);
        SQLiteDatabase db = appDbRouteService.getWritableDatabase();
        routeAdapter = new RouteAdapter(context, R.layout.display_route_row_layout);

        for (int i = 0; i < serviceStopModel.getRoutes().size(); i++) {
            RouteUiModel route = routeUiModelFactory.getRouteUIModel(serviceStopModel.getRoutes().get(i));
            //delete entry if it already exists, prevents duplicates
            appDbRouteService.deleteRow(db, serviceStopModel.getStopNo(), route);
            appDbRouteService.insertRow(db, serviceStopModel.getStopNo(), route);
            publishProgress(route);
        }

        return UiUtils.RouteBackgroundTaskReturnValues.ROUTES_RETRIEVED_FROM_STOP_OBJECT.name();
    }

    @Override
    protected void onProgressUpdate(RouteUiModel... values) {
        routeAdapter.add(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        UiUtils.RouteBackgroundTaskReturnValues inputCase =
                UiUtils.RouteBackgroundTaskReturnValues.valueOf(result);
        try {
            switch (inputCase) {
                case ROUTES_RETRIEVED_FROM_APP_DB:
                    if (routeAdapter.getCount() > 0) {
                        listView.setAdapter(routeAdapter);
                        callback.backGroundTaskSuccess(UiUtils.RouteBackgroundTaskReturnValues.ROUTES_RETRIEVED_FROM_APP_DB.name());
                    } else {
                        callback.backGroundTaskSuccess(UiUtils.RouteBackgroundTaskReturnValues.EMPTY.name());
                    }
                    return;
                case ROUTES_RETRIEVED_FROM_STOP_OBJECT:
                    if (routeAdapter.getCount() > 0) {
                        listView.setAdapter(routeAdapter);
                        callback.backGroundTaskSuccess(UiUtils.RouteBackgroundTaskReturnValues.ROUTES_RETRIEVED_FROM_STOP_OBJECT.name());
                    } else {
                        callback.backGroundTaskSuccess(UiUtils.RouteBackgroundTaskReturnValues.EMPTY.name());
                    }
                    return;
                case STOP_UPDATED_IN_APP_DB:
                    return;
                case STOP_RETRIEVED_FROM_OC_DB:
                    return;
                case STOP_ADDED_TO_APP_DB:
                    return;
                case APP_DB_CHECKED_FOR_STOP:
                    return;
            }
        }
        catch(Exception e) {
            callback.backGroundTaskFailure(new Exception(result));
        }
    }

}
