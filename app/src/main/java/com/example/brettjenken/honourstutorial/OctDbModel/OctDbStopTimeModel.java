package com.example.brettjenken.honourstutorial.OctDbModel;

/**
 * Created by Brett on 2/20/2017.
 */

public class OctDbStopTimeModel {
    String tripId, arrivalTime, stopId;

    public OctDbStopTimeModel() {
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }
}
