package com.example.brettjenken.honourstutorial.ServiceModel;

/**
 * Created by Brett on 1/21/2017.
 */

public class ServiceTripModel {

    public ServiceTripModel() {
    }

    private String tripDestination;
    private String tripStartTime;
    private String adjustedScheduleTime;
    private String busType;

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(String tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public String getAdjustedScheduleTime() {
        return adjustedScheduleTime;
    }

    public void setAdjustedScheduleTime(String adjustedScheduleTime) {
        this.adjustedScheduleTime = adjustedScheduleTime;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

}
