package com.example.brettjenken.honourstutorial.Stop;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.brettjenken.honourstutorial.Factory.StopUIModelFactory;
import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Service.DBStopService;
import com.example.brettjenken.honourstutorial.UIBackgroundTaskCallback;

/**
 * Created by Brett on 11/24/2016.
 */
public class StopsBackgroundTask extends AsyncTask<String, StopUIModel, String> {
    Context context;
    StopAdapter stopAdapter;
    //RouteAdapter routeAdapter;
    Activity activity;
    ListView listView;
    UIBackgroundTaskCallback callback;
    DBStopService dbStopService;
    StopUIModelFactory stopUIModelFactory;
    StopsBackgroundTask(UIBackgroundTaskCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
        this.activity = (Activity)context;
        this.stopUIModelFactory = new StopUIModelFactory();
        this.dbStopService = new DBStopService(context);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        if (params[0] == "get_all_stops") {
            SQLiteDatabase db = dbStopService.getReadableDatabase();
            String[] ids = {"1234", "5678"};
            String[] locations = {"Carleton", "Rideau Centre"};
            String[] routes = {"4, 7, 111", "95, 65, 39"};
            //generates mock data
            for(int i = 0; i < 2; i++){
                StopUIModel stop = new StopUIModel();
                stop.setId(ids[i]);
                stop.setLocation(locations[i]);
                stop.setRoutes(routes[i]);
                //comment out after data is successfully added
                //dbStopService.insertRow(db, stop);
            }
            //gets the table entries
            Cursor cursor = dbStopService.getAllEntries(db);
            listView = (ListView) activity.findViewById(R.id.stopsListListView);
            stopAdapter = new StopAdapter(context, R.layout.display_stop_row_layout);
            while(cursor.moveToNext()){
                publishProgress(stopUIModelFactory.getStopUIModel(cursor));
            }


            return "get_all_stops";
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(StopUIModel... values) {
        stopAdapter.add(values[0]);
    }


    @Override
    protected void onPostExecute(String result) {
        if(result.equals("get_all_stops")){
            listView.setAdapter(stopAdapter);
            callback.backGroundTaskSuccess("");
        }else
            callback.backGroundTaskFailure(new Exception(result));
    }
}
