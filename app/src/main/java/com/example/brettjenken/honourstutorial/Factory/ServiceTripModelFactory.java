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

}
