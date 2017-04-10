package com.example.brettjenken.honourstutorial.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.brettjenken.honourstutorial.Dao.OctDbStopDao;
import com.example.brettjenken.honourstutorial.Factory.StopUiModelFactory;


/**
 * Created by Brett on 2/18/2017.
 */

public class OctDbService {
    OctDbStopDao octDbStopDao;
    StopUiModelFactory stopUiModelFactory;
    public OctDbService(Context context) {
        this.octDbStopDao = new OctDbStopDao(context);
        this.stopUiModelFactory = new StopUiModelFactory();
    }

    public boolean checkForStop(SQLiteDatabase db, String stopCode){
        Cursor cursor = octDbStopDao.getStop(db, stopCode);
        boolean output = cursor.moveToFirst();
        return output;
    }

    public SQLiteDatabase getReadableDatabase(){
        return octDbStopDao.getReadableDatabase();
    }

}
