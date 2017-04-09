package com.example.brettjenken.honourstutorial.Route;

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
public class RoutesBackgroundTask extends AsyncTask<String, RouteUIModel, String>{
    Context context;
    RouteAdapter routeAdapter;
    Activity activity;
    ListView listView;
    UIBackgroundTaskCallback callback;

    RoutesBackgroundTask(UIBackgroundTaskCallback callback, Context context) {
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
        if (params[0] == "get_all_routes") {
            listView = (ListView) activity.findViewById(R.id.routesListListView);
            String num, direction;
            String[] nums = {"4", "8"};
            String[] directions = {"Northbound", "Eastbound"};

            routeAdapter = new RouteAdapter(context, R.layout.display_route_row_layout);

            for (int i = 0; i < 2; i++) {
                RouteUIModel route = new RouteUIModel();
                route.setNumber(nums[i]);
                route.setDirection(directions[i]);
                publishProgress(route);
            }

            return "get_all_routes";
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(RouteUIModel... values) {
        routeAdapter.add(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("get_all_routes") || result.equals("get_routes_from_db")){
            if (routeAdapter.getCount() > 0) {
                listView.setAdapter(routeAdapter);
                callback.backGroundTaskSuccess("");
            }else{
                callback.backGroundTaskSuccess("Empty");
            }
        }else
            callback.backGroundTaskFailure(new Exception(result));
    }

}
