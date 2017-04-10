package com.example.brettjenken.honourstutorial.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.brettjenken.honourstutorial.DAO.DBStopDao;
import com.example.brettjenken.honourstutorial.Factory.StopUIModelFactory;
import com.example.brettjenken.honourstutorial.Stop.StopUIModel;


/**
 * Created by Brett on 2/18/2017.
 */

public class DBStopService {

    DBStopDao dbStopDao;
    StopUIModelFactory stopUIModelFactory;
    public DBStopService(Context context) {
        dbStopDao = new DBStopDao(context);
        stopUIModelFactory = new StopUIModelFactory();
    }

    public void insertRow(SQLiteDatabase db, StopUIModel stop){
        if (stop.getId() != null) {
            dbStopDao.insertRow(db,
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
        dbStopDao.deleteRow(db, stopNumber);
    }

    public void updateAccessedTime(SQLiteDatabase db, String stopNumber){
        dbStopDao.updateAccessedTime(db, stopNumber, System.currentTimeMillis());
    }
    public Cursor getAllEntries(SQLiteDatabase db){
        return dbStopDao.getAllEntries(db);

    }

    public boolean stopInTable(SQLiteDatabase db, String stopNum){
        return dbStopDao.stopInTable(db, stopNum);
    }

    public SQLiteDatabase getReadableDatabase(){
        return dbStopDao.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return dbStopDao.getWritableDatabase();
    }

}
