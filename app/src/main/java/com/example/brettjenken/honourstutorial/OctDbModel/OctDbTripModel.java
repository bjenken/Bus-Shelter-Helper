package com.example.brettjenken.honourstutorial.OctDbModel;

/**
 * Created by Brett on 2/20/2017.
 */

public class OctDbTripModel {
    String routeId, tripHeadsign, direction;

    public OctDbTripModel() {
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public void setTripHeadsign(String tripHeadsign) {
        this.tripHeadsign = tripHeadsign;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
