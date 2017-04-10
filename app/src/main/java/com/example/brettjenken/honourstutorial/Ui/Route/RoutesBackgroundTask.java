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
import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;
import com.example.brettjenken.honourstutorial.Ui.UiUtils;

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
    RouteUiModelFactory routeUiModelFactory;
    RoutesBackgroundTask(UiBackgroundTaskCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
        this.activity = (Activity)context;
        this.appDbRouteService = new AppDbRouteService(context);
        this.routeUiModelFactory = new RouteUiModelFactory();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        UiUtils.RouteBackGroundTaskInputValues inputCase =
                UiUtils.RouteBackGroundTaskInputValues.valueOf(params[0]);
        switch(inputCase){
            case GET_ROUTES_FROM_APP_DB:
                return this.getRoutesFromAppDb();
        }
        return null;
    }

    private String getRoutesFromAppDb(){
        listView = (ListView) activity.findViewById(R.id.routesListListView);
        SQLiteDatabase db = appDbRouteService.getReadableDatabase();
        String stopNum = "1785";
        String[] nums = {"4", "8"};
        String[] directions = {"Northbound", "Eastbound"};
        for (int i = 0; i < 2; i++) {
            RouteUiModel route = new RouteUiModel();
            route.setNumber(nums[i]);
            route.setDirection(directions[i]);
            //appDbRouteService.insertRow(db, stopNum, route);
        }

        Cursor cursor = appDbRouteService.getAllEntriesForStop(db, stopNum);
        while(cursor.moveToNext()){
            publishProgress(routeUiModelFactory.getRouteUIModel(cursor));
        }
        routeAdapter = new RouteAdapter(context, R.layout.display_route_row_layout);

        return UiUtils.RouteBackGroundTaskInputValues.GET_ROUTES_FROM_APP_DB.name();
    }
    @Override
    protected void onProgressUpdate(RouteUiModel... values) {
        routeAdapter.add(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        UiUtils.RouteBackGroundTaskInputValues inputCase =
                UiUtils.RouteBackGroundTaskInputValues.valueOf(result);
        switch(inputCase){
            case GET_ROUTES_FROM_APP_DB:
                if (routeAdapter.getCount() > 0) {
                    listView.setAdapter(routeAdapter);
                    callback.backGroundTaskSuccess(UiUtils.RouteBackGroundTaskReturnValues.ROUTES_RETRIEVED.name());
                }else{
                    callback.backGroundTaskSuccess(UiUtils.RouteBackGroundTaskReturnValues.EMPTY.name());
                }
                return;
        }
        callback.backGroundTaskFailure(new Exception(result));
    }

}
