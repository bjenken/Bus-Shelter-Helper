package com.example.brettjenken.honourstutorial.Ui.Stop;

/**
 * Created by Brett on 11/24/2016.
 */
public class StopUiModel {
    public StopUiModel(){
    }


    private String id;
    private String location;
    private String routes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
