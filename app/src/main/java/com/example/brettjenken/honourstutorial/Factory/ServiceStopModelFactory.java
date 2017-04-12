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

    public ServiceStopModel getServiceStopModel(){
        return new ServiceStopModel();
    }


    public ServiceStopModel getStopWithRoutes(OctDbStopModel stop, List<ServiceRouteModel> routes){
        ServiceStopModel output = this.getServiceStopModel();
        output.setRoutes(routes);
        output.setStopDescription(stop.getStopName());
        output.setStopNo(stop.getStopCode());
        return output;
    }

    public ServiceStopModel getTripsForStop(JSONObject data){
        ServiceStopModel output = this.getServiceStopModel();
        output.setStopNo(data.optString("StopNo"));
        output.setStopDescription(data.optString("StopDescription"));
        try {
            output.addRoute(serviceRouteModelFactory.getTripsForStop(data.optJSONObject("Route").getJSONObject("RouteDirection")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return output;
    }

    public ServiceStopModel getTripsForMultiRouteStop(JSONObject data, String direction){
        ServiceStopModel output = this.getServiceStopModel();
        output.setStopNo(data.optString("StopNo"));
        output.setStopDescription(data.optString("StopDescription"));
        JSONObject routeObj = null;
        JSONArray jsonArr = null;
        try {
            jsonArr = data.optJSONObject("Route").getJSONArray("RouteDirection");
            for (int i = 0; i < jsonArr.length(); i++) {
                //figure out which route we want based on the passed in direction
                String routeDir = jsonArr.getJSONObject(i).optString("RouteLabel");
                if (routeDir.equals(direction)) {
                    routeObj = jsonArr.getJSONObject(i);
                }
            }
            output.addRoute(serviceRouteModelFactory.getTripsForStop(routeObj));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return output;
    }
}
