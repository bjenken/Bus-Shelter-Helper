package com.example.brettjenken.honourstutorial.Times;

/**
 * Created by Brett on 11/24/2016.
 */
public class TimeUIModel {


    public TimeUIModel(String time, String destination) {
        this.setTime(time);
        this.setDestination(destination);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    private String time;
    private String destination;

}
