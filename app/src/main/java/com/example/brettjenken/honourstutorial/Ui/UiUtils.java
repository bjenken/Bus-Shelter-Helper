package com.example.brettjenken.honourstutorial.Ui;

/**
 * Created by Brett on 2/21/2017.
 */

public class UiUtils {
    public enum RouteBackgroundTaskInputValues {
        GET_ROUTES_FROM_STOP_OBJECT,
        ADD_STOP_TO_APP_DB,
        CHECK_APP_DB_FOR_STOP,
        GET_ROUTES_FROM_APP_DB,
        UPDATE_STOP_IN_APP_DB,
        GET_STOP_FROM_OC_DB
    }

    public enum RouteBackgroundTaskReturnValues {
        ROUTES_RETRIEVED_FROM_STOP_OBJECT,
        STOP_ADDED_TO_APP_DB,
        APP_DB_CHECKED_FOR_STOP,
        EMPTY,
        ROUTES_RETRIEVED_FROM_APP_DB,
        STOP_UPDATED_IN_APP_DB,
        STOP_RETRIEVED_FROM_OC_DB,
        STOP_FOUND,
        STOP_NOT_FOUND
    }

    public enum StopBackgroundTaskInputValues {
        GET_ALL_STOPS,
        CHECK_FOR_STOP
    }

    public enum StopBackgroundTaskReturnValues {
        STOPS_RETRIEVED,
        STOP_FOUND
    }

    public enum TimesBackgroundTaskInputValues{
        GET_ALL_TIMES
    }

    public enum TimesBackgroundTaskReturnValues{
        TIMES_RETRIEVED,
        EMPTY
    }

    public enum ApiServiceInputValues{
        GET_ALL_TIMES
    }

    public enum ApiServiceReturnValues{
        ALL_TIMES_RETRIEVED
    }
}
