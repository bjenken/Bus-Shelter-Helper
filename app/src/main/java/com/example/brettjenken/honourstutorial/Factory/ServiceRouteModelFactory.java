package com.example.brettjenken.honourstutorial.Factory;

import com.example.brettjenken.honourstutorial.OctDbModel.OctDbRouteModel;
import com.example.brettjenken.honourstutorial.OctDbModel.OctDbTripModel;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceRouteModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Brett on 2/18/2017.
 */

public class ServiceRouteModelFactory {
    ServiceTripModelFactory serviceTripModelFactory;
    public ServiceRouteModelFactory() {
        this.serviceTripModelFactory = new ServiceTripModelFactory();
    }

    public ServiceRouteModel getRouteAPIModel(){
        return new ServiceRouteModel();
    }


    public ServiceRouteModel getRoutesForStop(OctDbTripModel trip, OctDbRouteModel route){
        ServiceRouteModel output = this.getRouteAPIModel();
        output.setRouteNo(Integer.parseInt(route.getRouteShortName()));
        output.setDirectionId(Integer.parseInt(trip.getDirection()));
        output.setRouteHeading(trip.getTripHeadsign());
        output.setDirection(trip.getTripHeadsign());
        return output;
    }
}
