package com.example.brettjenken.honourstutorial.Ui;

/**
 * Created by Brett on 2/21/2017.
 */

public class UiUtils {
    public enum RouteBackGroundTaskInputValues{
        GET_ROUTES_FROM_APP_DB
    }

    public enum RouteBackGroundTaskReturnValues {
        EMPTY,
        ROUTES_RETRIEVED
    }

    public enum StopBackGroundTaskInputValues {
        GET_ALL_STOPS,
        CHECK_FOR_STOP
    }

    public enum StopBackGroundTaskReturnValues {
        STOPS_RETRIEVED,
        STOP_FOUND
    }
}
