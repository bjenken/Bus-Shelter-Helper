package com.example.brettjenken.honourstutorial.ServiceModel;

/**
 * Created by Brett on 1/21/2017.
 */

public class ServiceTripModel {

    public ServiceTripModel() {
    }

    private String tripDestination;
    private String adjustedScheduleTime;

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getAdjustedScheduleTime() {
        return adjustedScheduleTime;
    }

    public void setAdjustedScheduleTime(String adjustedScheduleTime) {
        this.adjustedScheduleTime = adjustedScheduleTime;
    }


}
