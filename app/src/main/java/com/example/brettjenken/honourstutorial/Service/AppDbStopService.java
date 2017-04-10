package com.example.brettjenken.honourstutorial.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.brettjenken.honourstutorial.Dao.AppDbStopDao;
import com.example.brettjenken.honourstutorial.Factory.StopUiModelFactory;
import com.example.brettjenken.honourstutorial.Ui.Stop.StopUiModel;


/**
 * Created by Brett on 2/18/2017.
 */

public class AppDbStopService {

    AppDbStopDao appDbStopDao;
    StopUiModelFactory stopUiModelFactory;
    public AppDbStopService(Context context) {
        appDbStopDao = new AppDbStopDao(context);
        stopUiModelFactory = new StopUiModelFactory();
    }

    public void insertRow(SQLiteDatabase db, StopUiModel stop){
        if (stop.getId() != null) {
            appDbStopDao.insertRow(db,
                    stop.getId(),
                    stop.getLocation(),
                    stop.getRoutes(),
                    System.currentTimeMillis()
            );
        }else{
            System.out.println("STOP NULL");
        }
    }

    public void deleteRow(SQLiteDatabase db, String stopNumber){
        appDbStopDao.deleteRow(db, stopNumber);
    }

    public void updateAccessedTime(SQLiteDatabase db, String stopNumber){
        appDbStopDao.updateAccessedTime(db, stopNumber, System.currentTimeMillis());
    }
    public Cursor getAllEntries(SQLiteDatabase db){
        return appDbStopDao.getAllEntries(db);

    }

    public boolean stopInTable(SQLiteDatabase db, String stopNum){
        return appDbStopDao.stopInTable(db, stopNum);
    }

    public SQLiteDatabase getReadableDatabase(){
        return appDbStopDao.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return appDbStopDao.getWritableDatabase();
    }

}
