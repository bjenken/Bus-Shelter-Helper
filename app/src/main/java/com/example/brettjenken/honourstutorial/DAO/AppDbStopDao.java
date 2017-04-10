package com.example.brettjenken.honourstutorial.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.brettjenken.honourstutorial.DBTable.AppDbRouteTableData;
import com.example.brettjenken.honourstutorial.DBTable.AppDbStopTableData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * Created by Brett on 2/11/2017.
 */

public class AppDbStopDao extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    public String createQuery = "CREATE TABLE " + AppDbStopTableData.TABLE_NAME
            + "("
            + AppDbStopTableData.STOP_NUMBER + " TEXT, "
            + AppDbStopTableData.LOCATION + " TEXT, "
            + AppDbStopTableData.ROUTES + " TEXT, "
            + AppDbStopTableData.LAST_ACCESSED + " INTEGER"
            + ");";
    public String createQueryRoutes = "CREATE TABLE " + AppDbRouteTableData.TABLE_NAME
            + "("
            + AppDbRouteTableData.STOP_NUMBER + " TEXT, "
            + AppDbRouteTableData.ROUTE_NUMBER + " TEXT, "
            + AppDbRouteTableData.DIRECTION + " TEXT"
            + ");";
    public AppDbStopDao(Context context) {
        super(context, AppDbStopTableData.DATABASE_NAME, null, databaseVersion);
        //this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS '" + AppDbStopTableData.TABLE_NAME + "'");
        //this.getWritableDatabase().execSQL(createQuery);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createQuery);
        //since after this the database created, the onCreate in the AppDbRouteDao will not fire
        db.execSQL(createQueryRoutes);
        Log.d("DB Stop Service", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(createQuery);
        Log.d("DB Stop Service", "Table Created");
    }

    public void insertRow(SQLiteDatabase db, String stopNumber, String location, String routes, long lastAccessed){
        ContentValues cv = new ContentValues();
        cv.put(AppDbStopTableData.STOP_NUMBER, stopNumber);
        cv.put(AppDbStopTableData.LOCATION, location);
        cv.put(AppDbStopTableData.ROUTES, routes);
        cv.put(AppDbStopTableData.LAST_ACCESSED, lastAccessed);

        long k = db.insert(AppDbStopTableData.TABLE_NAME, null, cv);
        Log.d("DB Stop Service", "1 Row Inserted");
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(SQLiteDatabase db, String stopNumber){
        db.delete(AppDbStopTableData.TABLE_NAME,
                  AppDbStopTableData.STOP_NUMBER + "=" + stopNumber,
                  null);
        Log.d("DB Stop Service", "1 Row Deleted");
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAccessedTime(SQLiteDatabase db, String stopNumber, long lastAccessed){
        ContentValues cv = new ContentValues();
        cv.put(AppDbStopTableData.LAST_ACCESSED, lastAccessed);
        db.update(AppDbStopTableData.TABLE_NAME, cv,
                  AppDbStopTableData.STOP_NUMBER + "= ?",
                  new String[] {stopNumber});
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("DB Stop Service", "1 Row Updated");
    }
    public Cursor getAllEntries(SQLiteDatabase db){
        String[] projections = {
                AppDbStopTableData.STOP_NUMBER,
                AppDbStopTableData.ROUTES,
                AppDbStopTableData.LOCATION
        };
        Cursor cursor = db.query(
                AppDbStopTableData.TABLE_NAME,
                projections,
                null,null,null,null,
                AppDbStopTableData.LAST_ACCESSED+" DESC");
        Log.d("DB Stop Service", "Entries Retrieved");
        return cursor;

    }

    public boolean stopInTable(SQLiteDatabase db, String stopNum){
        Cursor c = db.rawQuery(
                "select * from "
                        + AppDbStopTableData.TABLE_NAME
                        + " where "
                        + AppDbStopTableData.STOP_NUMBER
                        + "=?",
                new String[] { stopNum });
        return c.getCount() != 0;
    }

    //for Debug purposes
    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            String currentDBPath = AppDbStopTableData.DATABASE_NAME;
            String backupDBPath = "stopTableBackup.db";
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
