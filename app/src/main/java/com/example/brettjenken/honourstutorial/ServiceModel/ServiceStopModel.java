package com.example.brettjenken.honourstutorial.ServiceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brett on 1/21/2017.
 */

public class ServiceStopModel {

    public ServiceStopModel() {
        routes = new ArrayList<ServiceRouteModel>();
    }

    String  stopNo,
            stopDescription;

    List<ServiceRouteModel> routes;

    public String getStopNo() {
        return stopNo;
    }

    public void setStopNo(String stopNo) {
        this.stopNo = stopNo;
    }

    public String getStopDescription() {
        return stopDescription;
    }

    public void setStopDescription(String stopDescription) {
        this.stopDescription = stopDescription;
    }


    public List<ServiceRouteModel> getRoutes() {
        return routes;
    }

    public void setRoutes(List<ServiceRouteModel> routes) {
        this.routes = routes;
    }

    public void addRoute(ServiceRouteModel route){
        this.routes.add(route);
    }

}
