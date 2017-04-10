package com.example.brettjenken.honourstutorial.Service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.brettjenken.honourstutorial.Dao.AppDbRouteDao;
import com.example.brettjenken.honourstutorial.Ui.Route.RouteUiModel;


/**
 * Created by Brett on 2/18/2017.
 */

public class AppDbRouteService {
    AppDbRouteDao appDbRouteDao;
    public AppDbRouteService(Context context) {
        appDbRouteDao = new AppDbRouteDao(context);
    }

    public void insertRow(SQLiteDatabase db, String stopNum, RouteUiModel route){
        appDbRouteDao.insertRow(db,
                stopNum,
                route.getNumber(),
                route.getDirection()
                );
    }

    public Cursor getAllEntriesForStop(SQLiteDatabase db, String stopNum){
        return appDbRouteDao.getAllEntriesForStop(db, stopNum);

    }
    public boolean stopInTable(SQLiteDatabase db, String stopNum){
        return appDbRouteDao.stopInTable(db, stopNum);
    }

    public SQLiteDatabase getReadableDatabase(){
        return appDbRouteDao.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return appDbRouteDao.getWritableDatabase();
    }

    public void deleteRow(SQLiteDatabase db, String stopNum, RouteUiModel route) {
        appDbRouteDao.deleteRow(db,
                stopNum,
                route.getNumber(),
                route.getDirection());
    }
}
