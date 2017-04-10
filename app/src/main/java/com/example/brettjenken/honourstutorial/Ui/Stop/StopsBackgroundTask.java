package com.example.brettjenken.honourstutorial.Ui.Stop;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.brettjenken.honourstutorial.Factory.StopUiModelFactory;
import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Service.AppDbStopService;
import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;
import com.example.brettjenken.honourstutorial.Ui.UiUtils;

/**
 * Created by Brett on 11/24/2016.
 */
public class StopsBackgroundTask extends AsyncTask<String, StopUiModel, String> {
    Context context;
    StopAdapter stopAdapter;
    //RouteAdapter routeAdapter;
    Activity activity;
    ListView listView;
    UiBackgroundTaskCallback callback;
    AppDbStopService appDbStopService;
    StopUiModelFactory stopUiModelFactory;
    StopsBackgroundTask(UiBackgroundTaskCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
        this.activity = (Activity)context;
        this.stopUiModelFactory = new StopUiModelFactory();
        this.appDbStopService = new AppDbStopService(context);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        UiUtils.StopBackGroundTaskInputValues inputCase =
                UiUtils.StopBackGroundTaskInputValues.valueOf(params[0]);
        switch(inputCase){
            case GET_ALL_STOPS:
                return this.getAllStops();
        }
        return null;
    }

    private String getAllStops(){
        SQLiteDatabase db = appDbStopService.getReadableDatabase();
        String[] ids = {"1234", "5678"};
        String[] locations = {"Carleton", "Rideau Centre"};
        String[] routes = {"4, 7, 111", "95, 65, 39"};
        //generates mock data
        for(int i = 0; i < 2; i++){
            StopUiModel stop = new StopUiModel();
            stop.setId(ids[i]);
            stop.setLocation(locations[i]);
            stop.setRoutes(routes[i]);
            //comment out after data is successfully added
            //appDbStopService.insertRow(db, stop);
        }
        //gets the table entries
        Cursor cursor = appDbStopService.getAllEntries(db);
        listView = (ListView) activity.findViewById(R.id.stopsListListView);
        stopAdapter = new StopAdapter(context, R.layout.display_stop_row_layout);
        while(cursor.moveToNext()){
            publishProgress(stopUiModelFactory.getStopUIModel(cursor));
        }
        return UiUtils.StopBackGroundTaskInputValues.GET_ALL_STOPS.name();
    }

    @Override
    protected void onProgressUpdate(StopUiModel... values) {
        stopAdapter.add(values[0]);
    }


    @Override
    protected void onPostExecute(String result) {
        UiUtils.StopBackGroundTaskInputValues inputCase =
                UiUtils.StopBackGroundTaskInputValues.valueOf(result);
        switch(inputCase){
            case GET_ALL_STOPS:
                listView.setAdapter(stopAdapter);
                callback.backGroundTaskSuccess("");
                return;
        }

        callback.backGroundTaskFailure(new Exception(result));
    }
}
