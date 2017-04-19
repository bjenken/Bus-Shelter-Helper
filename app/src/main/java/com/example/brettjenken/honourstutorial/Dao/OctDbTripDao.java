package com.example.brettjenken.honourstutorial.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbRouteTableData;
import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbTripTableData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;


/**
 * Created by Brett on 2/11/2017.
 */

public class OctDbTripDao extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    private Context context;
    public String createQuery = "CREATE TABLE " + OctDbTripTableData.TABLE_NAME
            + "("
            + OctDbTripTableData.ROUTE_ID + " TEXT, "
            + OctDbTripTableData.SERVICE_ID + " TEXT, "
            + OctDbTripTableData.TRIP_ID + " TEXT, "
            + OctDbTripTableData.TRIP_HEADSIGN + " TEXT, "
            + OctDbTripTableData.DIRECTION_ID + " TEXT, "
            + OctDbTripTableData.BLOCK_ID + " TEXT"
            + ");";

    public OctDbTripDao(Context context) {
        super(context, OctDbTripTableData.DATABASE_NAME, null, databaseVersion);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createQuery);
        this.importFromCSV(db);
        Log.d("OC DB Route Service", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(createQuery);
        Log.d("OC DB Route Service", "Table Created");
    }


    public void importFromCSV(SQLiteDatabase db){
        String mCSVfile = "trips.csv";
        AssetManager manager = this.context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.execSQL("DROP TABLE IF EXISTS '" + OctDbTripTableData.TABLE_NAME + "'");
        db.execSQL(createQuery);
        db.beginTransaction();
        boolean firstRowFlag = false;
        try {
            while ((line = buffer.readLine()) != null) {
                if (firstRowFlag != false) {
                    String[] colums = line.split(",", -1);
                    if (colums.length != 6) {
                        Log.d("CSVParser", "Skipping Bad CSV Row");
                        continue;
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(OctDbTripTableData.ROUTE_ID, colums[0].trim());
                    cv.put(OctDbTripTableData.SERVICE_ID, colums[1].trim());
                    cv.put(OctDbTripTableData.TRIP_ID, colums[2].trim());
                    cv.put(OctDbTripTableData.TRIP_HEADSIGN, colums[3].trim());
                    cv.put(OctDbTripTableData.DIRECTION_ID, colums[4].trim());
                    cv.put(OctDbTripTableData.BLOCK_ID, colums[5].trim());
                    db.insert(OctDbTripTableData.TABLE_NAME, null, cv);
                } else
                    firstRowFlag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

}
