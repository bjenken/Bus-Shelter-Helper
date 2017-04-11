package com.example.brettjenken.honourstutorial.ServiceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brett on 1/21/2017.
 */

public class ServiceRouteModel {

    public ServiceRouteModel() {
        trips = new ArrayList<ServiceTripModel>();
    }
    private int routeNo,
                directionId;
    private String direction,
            routeHeading;

    public List<ServiceTripModel> getTrips() {
        return trips;
    }

    public void setTrips(List<ServiceTripModel> trips) {
        this.trips = trips;
    }

    public void addTrip(ServiceTripModel trip) {
        this.trips.add(trip);
    }

    private List<ServiceTripModel> trips;

    public int getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(int routeNo) {
        this.routeNo = routeNo;
    }

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRouteHeading() {
        return routeHeading;
    }

    public void setRouteHeading(String routeHeading) {
        this.routeHeading = routeHeading;
    }

}
