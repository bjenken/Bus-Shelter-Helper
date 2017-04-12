package com.example.brettjenken.honourstutorial.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbRouteTableData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.List;


/**
 * Created by Brett on 2/11/2017.
 */

public class OctDbRouteDao extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    private Context context;
    public String createQuery = "CREATE TABLE " + OctDbRouteTableData.TABLE_NAME
            + "("
            + OctDbRouteTableData.ROUTE_ID + " TEXT, "
            + OctDbRouteTableData.ROUTE_SHORT_NAME + " TEXT, "
            + OctDbRouteTableData.ROUTE_LONG_NAME + " TEXT, "
            + OctDbRouteTableData.ROUTE_DESC + " TEXT, "
            + OctDbRouteTableData.ROUTE_TYPE+ " TEXT, "
            + OctDbRouteTableData.ROUTE_URL + " TEXT"
            + ");";

    public OctDbRouteDao(Context context) {
        super(context, OctDbRouteTableData.DATABASE_NAME, null, databaseVersion);
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

    public Cursor getRouteFromRouteId(SQLiteDatabase db, String routeId){
        //Since SQLite is iffy with the 'IN' clause, need to generate my own placeholders
        Cursor cursor = db.rawQuery(
                "select * from "
                        + OctDbRouteTableData.TABLE_NAME
                        + " where "
                        + OctDbRouteTableData.ROUTE_ID
                        + "=?",
                new String[]{routeId});
        cursor.moveToFirst();
        return cursor;
    }

    public void importFromCSV(SQLiteDatabase db){
        String mCSVfile = "routes.csv";
        AssetManager manager = this.context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.execSQL("DROP TABLE IF EXISTS '" + OctDbRouteTableData.TABLE_NAME + "'");
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
                    cv.put(OctDbRouteTableData.ROUTE_ID, colums[0].trim());
                    cv.put(OctDbRouteTableData.ROUTE_SHORT_NAME, colums[1].trim());
                    cv.put(OctDbRouteTableData.ROUTE_LONG_NAME, colums[2].trim());
                    cv.put(OctDbRouteTableData.ROUTE_DESC, colums[3].trim());
                    cv.put(OctDbRouteTableData.ROUTE_TYPE, colums[4].trim());
                    cv.put(OctDbRouteTableData.ROUTE_URL, colums[5].trim());
                    db.insert(OctDbRouteTableData.TABLE_NAME, null, cv);
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
