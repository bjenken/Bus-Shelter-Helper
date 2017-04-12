package com.example.brettjenken.honourstutorial.Service;

import android.os.AsyncTask;

import com.example.brettjenken.honourstutorial.Factory.ServiceStopModelFactory;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceStopModel;
import com.example.brettjenken.honourstutorial.Ui.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by Brett on 1/21/2017.
 */

public class ApiService extends AsyncTask<String, Void, String>{
    String jsonOutput;
    ServiceStopModelFactory serviceStopModelFactory;
    public ApiService(ServiceCallback serviceCallback) {
        this.serviceCallback = serviceCallback;
        this.serviceStopModelFactory = new ServiceStopModelFactory();
    }
    String routeNo, stopNo, direction;
    Boolean multiDirection;

    Exception error;
    ServiceCallback serviceCallback;

    @Override
    protected String doInBackground(String... params) {
        UiUtils.ApiServiceInputValues inputCase =
                UiUtils.ApiServiceInputValues.valueOf(params[0]);
        switch(inputCase){
            case GET_ALL_TIMES:
                stopNo = params[1];
                routeNo = params[2];
                direction = params[3];
                multiDirection = Boolean.parseBoolean(params[4]);
                return this.getAllTimes();

        }


        return null;
    }

    private String getAllTimes(){
        String appID = "e45d2afb"; //put this somewhere better
        String apiKey = "982dade09d232667c95a54eb6e674eca";

        String query = String.format("appID=%s&apiKey=%s&routeNo=%s&stopNo=%s&format=%s",
                appID,
                apiKey,
                routeNo,
                stopNo,
                "json");
        String endpoint = String.format("http://api.octranspo1.com/v1.2/GetNextTripsForStop?%s",
                query);

        //try to make the call to the API
        try {
            URL url = new URL(endpoint);
            URLConnection connection = url.openConnection();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            jsonOutput = result.toString();

            return UiUtils.ApiServiceReturnValues.ALL_TIMES_RETRIEVED.name();
        } catch (MalformedURLException e) {
            error = e;
        } catch (IOException e) {
            error = e;
        }

        return "Error Getting Times From Api";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null || error != null) {
            serviceCallback.serviceFailure(error);
            return;
        }

        UiUtils.ApiServiceReturnValues resultCase =
                UiUtils.ApiServiceReturnValues.valueOf(result);
        try{
            switch(resultCase){
                case ALL_TIMES_RETRIEVED:
                    JSONObject data = new JSONObject(jsonOutput);

                    JSONObject queryResults = data.optJSONObject("GetNextTripsForStopResult");
                    String error = queryResults.optString("Error");
                    int l = error.length();
                    if (l > 0) {
                        serviceCallback.serviceFailure(new Exception(
                                "No stop was found with the stop number: " + stopNo));
                        return;
                    }

                    ServiceStopModel stop;
                    if (!multiDirection)
                        stop = serviceStopModelFactory.getTripsForStop(queryResults);
                    else
                        stop = serviceStopModelFactory.getTripsForMultiRouteStop(queryResults, direction);

                    serviceCallback.serviceSuccess(stop);
                    return;
            }


        } catch (Exception e) {
                serviceCallback.serviceFailure(new Exception(result));
        }
    }
}
