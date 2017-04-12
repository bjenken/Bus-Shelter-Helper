package com.example.brettjenken.honourstutorial.Factory;

import com.example.brettjenken.honourstutorial.ServiceModel.ServiceTripModel;

import org.json.JSONObject;

import java.util.Calendar;


/**
 * Created by Brett on 2/18/2017.
 */

public class ServiceTripModelFactory {
    public ServiceTripModelFactory() {
    }

    public ServiceTripModel getServiceTripModel(){
        return new ServiceTripModel();
    }

    public ServiceTripModel getTripAPIModel(JSONObject data){
        ServiceTripModel output = this.getServiceTripModel();
        output.setTripDestination(data.optString("TripDestination"));

        Calendar c = Calendar.getInstance();
        Calendar tmp = (Calendar) c.clone();
        tmp.add(Calendar.MINUTE, Integer.parseInt(data.optString("AdjustedScheduleTime")));
        Calendar updatedCal = tmp;
        int hours = updatedCal.get(Calendar.HOUR);
        int minutes = updatedCal.get(Calendar.MINUTE);
        if (minutes > 9)
            output.setAdjustedScheduleTime(hours + ":" + minutes);
        else
            output.setAdjustedScheduleTime(hours + ":0" + minutes);

        return output;
    }
}
