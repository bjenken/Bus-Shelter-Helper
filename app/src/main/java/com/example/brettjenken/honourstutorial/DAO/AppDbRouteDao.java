package com.example.brettjenken.honourstutorial.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.brettjenken.honourstutorial.DBTable.AppDbRouteTableData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * Created by Brett on 2/11/2017.
 */

public class AppDbRouteDao extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    public String createQuery = "CREATE TABLE " + AppDbRouteTableData.TABLE_NAME
            + "("
            + AppDbRouteTableData.STOP_NUMBER + " TEXT, "
            + AppDbRouteTableData.ROUTE_NUMBER + " TEXT, "
            + AppDbRouteTableData.DIRECTION + " TEXT"
            + ");";

    public AppDbRouteDao(Context context) {
        super(context, AppDbRouteTableData.DATABASE_NAME, null, databaseVersion);
        //this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS '" + AppDbRouteTableData.TABLE_NAME + "'");
        //this.getWritableDatabase().execSQL(createQuery);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createQuery);
        Log.d("DB Route Service", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(createQuery);
        Log.d("DB Route Service", "Table Created");
    }

    public void insertRow(SQLiteDatabase db, String stopNumber, String routeNumber, String direction){
        ContentValues cv = new ContentValues();
        cv.put(AppDbRouteTableData.STOP_NUMBER, stopNumber);
        cv.put(AppDbRouteTableData.ROUTE_NUMBER, routeNumber);
        cv.put(AppDbRouteTableData.DIRECTION, direction);

        long k = db.insert(AppDbRouteTableData.TABLE_NAME, null, cv);
        Log.d("DB Route Service", "1 Row Inserted");
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cursor getAllEntriesForStop(SQLiteDatabase db, String stopNum){
        Cursor c = db.rawQuery(
                "select * from "
                        + AppDbRouteTableData.TABLE_NAME
                        + " where "
                        + AppDbRouteTableData.STOP_NUMBER
                        + "=?",
                new String[] { stopNum });
        Log.d("DB Route Service", "Entries Retrieved");
        return c;

    }
    public boolean stopInTable(SQLiteDatabase db, String stopNum){
        //db.execSQL("DROP TABLE IF EXISTS '" + AppDbRouteTableData.TABLE_NAME + "'");
        //db.execSQL(createQuery);
        Cursor c = db.rawQuery(
                "select * from "
                        + AppDbRouteTableData.TABLE_NAME
                        + " where "
                        + AppDbRouteTableData.STOP_NUMBER
                        + "=?",
                new String[] { stopNum });
        return c.getCount() != 0;
    }

    public void deleteRow(SQLiteDatabase db, String stopNum, String number, String direction) {
        db.delete(  AppDbRouteTableData.TABLE_NAME,
                    AppDbRouteTableData.STOP_NUMBER + "=? and "
                            + AppDbRouteTableData.ROUTE_NUMBER + "=? and "
                            + AppDbRouteTableData.DIRECTION + "=?"
                            , new String[]{stopNum, number, direction});
        Log.d("DB Route Service", "1 Row Deleted");
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //for Debug purposes
    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            String currentDBPath = AppDbRouteTableData.DATABASE_NAME;
            String backupDBPath = "routeTableBackup.db";
            File currentDB = new File("/data/data/brettjenken.busshelterhelper/databases/", currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        }
    }
}
