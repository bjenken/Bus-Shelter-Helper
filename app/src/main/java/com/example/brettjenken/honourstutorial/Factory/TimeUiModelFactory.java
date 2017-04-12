package com.example.brettjenken.honourstutorial.Factory;

import com.example.brettjenken.honourstutorial.ServiceModel.ServiceTripModel;
import com.example.brettjenken.honourstutorial.Ui.Times.TimeUiModel;

/**
 * Created by BrettJenken on 4/12/2017.
 */

public class TimeUiModelFactory {
    public TimeUiModelFactory(){

    }

    public TimeUiModel getTimeUiModel(){
        return new TimeUiModel();
    }

    public TimeUiModel getTimeUiModel(ServiceTripModel input){
        TimeUiModel output = this.getTimeUiModel();
        output.setDestination(input.getTripDestination());
        output.setTime(input.getAdjustedScheduleTime());
        return output;
    }
}
