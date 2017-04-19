package com.example.brettjenken.honourstutorial.Ui.Times;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.brettjenken.honourstutorial.Factory.TimeUiModelFactory;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceRouteModel;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceStopModel;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceTripModel;
import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;

import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Ui.UiUtils;

import java.util.List;


/**
 * Created by Brett on 11/24/2016.
 */
public class TimesBackgroundTask extends AsyncTask<String, TimeUiModel, String> {
    Context context;
    TimeAdapter timeAdapter;
    Activity activity;
    ListView listView;
    UiBackgroundTaskCallback callback;
    ServiceStopModel serviceStopModel;
    TimeUiModelFactory timeUiModelFactory;
    TimesBackgroundTask(UiBackgroundTaskCallback callback, Context context, ServiceStopModel stop) {
        this.callback = callback;
        this.context = context;
        this.activity = (Activity)context;
        this.serviceStopModel = stop;
        this.timeUiModelFactory = new TimeUiModelFactory();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        UiUtils.TimesBackgroundTaskInputValues inputCase =
                UiUtils.TimesBackgroundTaskInputValues.valueOf(params[0]);
        switch(inputCase){
            case GET_ALL_TIMES:
                return getAllTimes();
        }
        return null;
    }

    private String getAllTimes(){
        listView = (ListView) activity.findViewById(R.id.timesListListView);
        timeAdapter = new TimeAdapter(context, R.layout.display_time_row_layout);

        ServiceRouteModel stopRoute;
        if (serviceStopModel.getRoutes().size() > 0)
            stopRoute = serviceStopModel.getRoutes().get(0);
        else
            return UiUtils.TimesBackgroundTaskReturnValues.EMPTY.name();
        List<ServiceTripModel> stopTrips = stopRoute.getTrips();
        for (int i = 0; i < stopTrips.size(); i++) {
            publishProgress(timeUiModelFactory.getTimeUiModel(stopTrips.get(i)));
        }

        return UiUtils.TimesBackgroundTaskReturnValues.TIMES_RETRIEVED.name();
    }


    @Override
    protected void onProgressUpdate(TimeUiModel... values) {
        timeAdapter.add(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        UiUtils.TimesBackgroundTaskReturnValues resultCase =
                UiUtils.TimesBackgroundTaskReturnValues.valueOf(result);
        try{
            switch(resultCase){
                case TIMES_RETRIEVED:
                    if (timeAdapter.getCount() > 0) {
                        listView.setAdapter(timeAdapter);
                        callback.backGroundTaskSuccess(UiUtils.TimesBackgroundTaskReturnValues.TIMES_RETRIEVED.name());
                    }
                    return;
                case EMPTY:
                    callback.backGroundTaskSuccess(UiUtils.TimesBackgroundTaskReturnValues.EMPTY.name());
                    return;
            }
        } catch(Exception e){
            callback.backGroundTaskFailure(new Exception(result));
        }
    }
}
