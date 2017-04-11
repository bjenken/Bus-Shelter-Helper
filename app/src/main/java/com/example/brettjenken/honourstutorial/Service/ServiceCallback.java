package com.example.brettjenken.honourstutorial.Service;


import com.example.brettjenken.honourstutorial.ServiceModel.ServiceStopModel;

/**
 * Created by Brett on 1/21/2017.
 */

public interface ServiceCallback {
    void serviceSuccess(ServiceStopModel stop);
    void serviceFailure(Exception e);
}
