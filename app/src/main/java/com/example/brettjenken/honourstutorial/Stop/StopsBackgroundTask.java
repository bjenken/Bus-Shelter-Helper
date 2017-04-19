package com.example.brettjenken.honourstutorial.Stop;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.UIBackgroundTaskCallback;

/**
 * Created by Brett on 11/24/2016.
 */
public class StopsBackgroundTask extends AsyncTask<String, StopUIModel, String> {
    Context context;
    StopAdapter stopAdapter;
    Activity activity;
    ListView listView;
    UIBackgroundTaskCallback callback;

    StopsBackgroundTask(UIBackgroundTaskCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
        this.activity = (Activity)context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        if (params[0] == "get_all_stops") {
            String[] ids = {"1234", "5678"};
            String[] locations = {"Carleton", "Rideau Centre"};
            String[] routes = {"4, 7, 111", "95, 65, 39"};
            listView = (ListView) activity.findViewById(R.id.stopsListListView);
            stopAdapter = new StopAdapter(context, R.layout.display_stop_row_layout);

            for(int i = 0; i < 2; i++){
                StopUIModel stop = new StopUIModel();
                stop.setId(ids[i]);
                stop.setLocation(locations[i]);
                stop.setRoutes(routes[i]);
                publishProgress(stop);
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
