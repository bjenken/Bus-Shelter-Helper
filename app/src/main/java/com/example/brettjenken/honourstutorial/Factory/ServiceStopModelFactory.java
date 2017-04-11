package com.example.brettjenken.honourstutorial.Factory;

import com.example.brettjenken.honourstutorial.OctDbModel.OctDbStopModel;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceRouteModel;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceStopModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



/**
 * Created by Brett on 2/18/2017.
 */

public class ServiceStopModelFactory {
    ServiceRouteModelFactory serviceRouteModelFactory;
    public ServiceStopModelFactory() {
        this.serviceRouteModelFactory = new ServiceRouteModelFactory();
    }

    public ServiceStopModel getStopApiModel(){
        return new ServiceStopModel();
    }


    public ServiceStopModel getStopWithRoutes(OctDbStopModel stop, List<ServiceRouteModel> routes){
        ServiceStopModel output = this.getStopApiModel();
        output.setRoutes(routes);
        output.setStopDescription(stop.getStopName());
        output.setStopNo(stop.getStopCode());
        return output;
    }
}
