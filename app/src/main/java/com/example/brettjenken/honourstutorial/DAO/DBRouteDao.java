package com.example.brettjenken.honourstutorial.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.brettjenken.honourstutorial.DBTable.RouteTableData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * Created by Brett on 2/11/2017.
 */

public class DBRouteDao extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    public String createQuery = "CREATE TABLE " + RouteTableData.TABLE_NAME
            + "("
            + RouteTableData.STOP_NUMBER + " TEXT, "
            + RouteTableData.ROUTE_NUMBER + " TEXT, "
            + RouteTableData.DIRECTION + " TEXT"
            + ");";

    public DBRouteDao(Context context) {
        super(context, RouteTableData.DATABASE_NAME, null, databaseVersion);
        //this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS '" + RouteTableData.TABLE_NAME + "'");
        //this.getWritableDatabase().execSQL(createQuery);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createQuery);
        Log.d("DB Route com.example.brettjenken.honourstutorial.Service", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(createQuery);
        Log.d("DB Route com.example.brettjenken.honourstutorial.Service", "Table Created");
    }

    public void insertRow(SQLiteDatabase db, String stopNumber, String routeNumber, String direction){
        ContentValues cv = new ContentValues();
        cv.put(RouteTableData.STOP_NUMBER, stopNumber);
        cv.put(RouteTableData.ROUTE_NUMBER, routeNumber);
        cv.put(RouteTableData.DIRECTION, direction);

        long k = db.insert(RouteTableData.TABLE_NAME, null, cv);
        Log.d("DB Route com.example.brettjenken.honourstutorial.Service", "1 Row Inserted");
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cursor getAllEntriesForStop(SQLiteDatabase db, String stopNum){
        Cursor c = db.rawQuery(
                "select * from "
                        + RouteTableData.TABLE_NAME
                        + " where "
                        + RouteTableData.STOP_NUMBER
                        + "=?",
                new String[] { stopNum });
        Log.d("DB Route com.example.brettjenken.honourstutorial.Service", "Entries Retrieved");
        return c;

    }
    public boolean stopInTable(SQLiteDatabase db, String stopNum){
        //db.execSQL("DROP TABLE IF EXISTS '" + RouteTableData.TABLE_NAME + "'");
        //db.execSQL(createQuery);
        Cursor c = db.rawQuery(
                "select * from "
                        + RouteTableData.TABLE_NAME
                        + " where "
                        + RouteTableData.STOP_NUMBER
                        + "=?",
                new String[] { stopNum });
        return c.getCount() != 0;
    }

    //for Debug purposes
    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            String currentDBPath = RouteTableData.DATABASE_NAME;
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
