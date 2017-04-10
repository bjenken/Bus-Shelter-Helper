package com.example.brettjenken.honourstutorial.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.brettjenken.honourstutorial.DBTable.RouteTableData;
import com.example.brettjenken.honourstutorial.DBTable.StopTableData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * Created by Brett on 2/11/2017.
 */

public class DBStopDao extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    public String createQuery = "CREATE TABLE " + StopTableData.TABLE_NAME
            + "("
            + StopTableData.STOP_NUMBER + " TEXT, "
            + StopTableData.LOCATION + " TEXT, "
            + StopTableData.ROUTES + " TEXT, "
            + StopTableData.LAST_ACCESSED + " INTEGER"
            + ");";
    public String createQueryRoutes = "CREATE TABLE " + RouteTableData.TABLE_NAME
            + "("
            + RouteTableData.STOP_NUMBER + " TEXT, "
            + RouteTableData.ROUTE_NUMBER + " TEXT, "
            + RouteTableData.DIRECTION + " TEXT"
            + ");";
    public DBStopDao(Context context) {
        super(context, StopTableData.DATABASE_NAME, null, databaseVersion);
        //this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS '" + StopTableData.TABLE_NAME + "'");
        //this.getWritableDatabase().execSQL(createQuery);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createQuery);
        //since after this the database created, the onCreate in the DBRouteDao will not fire
        db.execSQL(createQueryRoutes);
        Log.d("DB Stop com.example.brettjenken.honourstutorial.Service", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(createQuery);
        Log.d("DB Stop com.example.brettjenken.honourstutorial.Service", "Table Created");
    }

    public void insertRow(SQLiteDatabase db, String stopNumber, String location, String routes, long lastAccessed){
        ContentValues cv = new ContentValues();
        cv.put(StopTableData.STOP_NUMBER, stopNumber);
        cv.put(StopTableData.LOCATION, location);
        cv.put(StopTableData.ROUTES, routes);
        cv.put(StopTableData.LAST_ACCESSED, lastAccessed);

        long k = db.insert(StopTableData.TABLE_NAME, null, cv);
        Log.d("DB Stop com.example.brettjenken.honourstutorial.Service", "1 Row Inserted");
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(SQLiteDatabase db, String stopNumber){
        db.delete(StopTableData.TABLE_NAME,
                  StopTableData.STOP_NUMBER + "=" + stopNumber,
                  null);
        Log.d("DB Stop com.example.brettjenken.honourstutorial.Service", "1 Row Deleted");
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAccessedTime(SQLiteDatabase db, String stopNumber, long lastAccessed){
        ContentValues cv = new ContentValues();
        cv.put(StopTableData.LAST_ACCESSED, lastAccessed);
        db.update(StopTableData.TABLE_NAME, cv,
                  StopTableData.STOP_NUMBER + "= ?",
                  new String[] {stopNumber});
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("DB Stop com.example.brettjenken.honourstutorial.Service", "1 Row Updated");
    }
    public Cursor getAllEntries(SQLiteDatabase db){
        String[] projections = {
                StopTableData.STOP_NUMBER,
                StopTableData.ROUTES,
                StopTableData.LOCATION
        };
        Cursor cursor = db.query(
                StopTableData.TABLE_NAME,
                projections,
                null,null,null,null,
                StopTableData.LAST_ACCESSED+" DESC");
        Log.d("DB Stop com.example.brettjenken.honourstutorial.Service", "Entries Retrieved");
        return cursor;

    }

    public boolean stopInTable(SQLiteDatabase db, String stopNum){
        Cursor c = db.rawQuery(
                "select * from "
                        + StopTableData.TABLE_NAME
                        + " where "
                        + StopTableData.STOP_NUMBER
                        + "=?",
                new String[] { stopNum });
        return c.getCount() != 0;
    }

    //for Debug purposes
    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            String currentDBPath = StopTableData.DATABASE_NAME;
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
