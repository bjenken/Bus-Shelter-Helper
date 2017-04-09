package com.example.brettjenken.honourstutorial;

/**
 * Created by Brett on 1/21/2017.
 */

public interface UIBackgroundTaskCallback {
    void backGroundTaskSuccess(String result);
    void backGroundTaskFailure(Exception e);
}
