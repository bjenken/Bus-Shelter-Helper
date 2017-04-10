package com.example.brettjenken.honourstutorial.Ui.Times;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;

import com.example.brettjenken.honourstutorial.R;


/**
 * Created by Brett on 11/24/2016.
 */
public class TimesBackgroundTask extends AsyncTask<String, TimeUIModel, String> {
    Context context;
    TimeAdapter timeAdapter;
    Activity activity;
    ListView listView;
    UiBackgroundTaskCallback callback;

    TimesBackgroundTask(UiBackgroundTaskCallback callback, Context context) {
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
        if (params[0] == "get_all_times") {
            listView = (ListView) activity.findViewById(R.id.timesListListView);
            String timeStr, destination;
            timeAdapter = new TimeAdapter(context, R.layout.display_time_row_layout);
            String[] times = {"12:34", "2:23"};
            String[] locations = {"Billings Bridge", "South Keys"};
            for (int i = 0; i < 2; i++) {
                TimeUIModel time = new TimeUIModel(times[i], locations[i]);
                publishProgress(time);
            }

            return "get_all_times";
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(TimeUIModel... values) {
        timeAdapter.add(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("get_all_times")){
            if (timeAdapter.getCount() > 0) {
                listView.setAdapter(timeAdapter);
                callback.backGroundTaskSuccess("");
            }
            else
                callback.backGroundTaskSuccess("Empty");
        }else
           callback.backGroundTaskFailure(new Exception(result));
    }
}
