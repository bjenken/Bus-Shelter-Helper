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
import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbStopTableData;

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

public class OctDbStopDao extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    private Context context;
    private OctDbRouteDao octDbRouteDao;
    private OctDbStopTimeDao octDbStopTimeDao;
    private OctDbTripDao octDbTripDao;
    private SQLiteDatabase db;
    public String createQuery = "CREATE TABLE " + OctDbStopTableData.TABLE_NAME
            + "("
            + OctDbStopTableData.STOP_ID + " TEXT, "
            + OctDbStopTableData.STOP_CODE + " TEXT, "
            + OctDbStopTableData.STOP_NAME + " TEXT, "
            + OctDbStopTableData.STOP_DESC + " TEXT, "
            + OctDbStopTableData.STOP_LAT + " TEXT, "
            + OctDbStopTableData.STOP_LON + " TEXT, "
            + OctDbStopTableData.ZONE_ID + " TEXT, "
            + OctDbStopTableData.STOP_URL + " TEXT, "
            + OctDbStopTableData.LOCATION_TYPE + " TEXT"
            + ");";


    public OctDbStopDao(Context context) {
        super(context, OctDbStopTableData.DATABASE_NAME, null, databaseVersion);
        this.context = context;
        db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase dbInput) {
        initializeAllTables(dbInput);
        //importFromCSV(dbInput);
        Log.d("OC DB Stop Service", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //initializeAllTables(db);
        Log.d("OC DB Stop Service", "Table Upgraded");
    }

    private void initializeAllTables(SQLiteDatabase db){
        this.importFromCSV(db);
        Log.d("OC DB Stop Service", "Stop Table Created");
        octDbRouteDao = new OctDbRouteDao(context);
        octDbRouteDao.importFromCSV(db);
        Log.d("OC DB Stop Service", "Route Table Created");
        octDbStopTimeDao = new OctDbStopTimeDao(context);
        octDbStopTimeDao.importFromCSV(db);
        Log.d("OC DB Stop Service", "Stop Time Table Created");
        octDbTripDao = new OctDbTripDao(context);
        octDbTripDao.importFromCSV(db);
        Log.d("OC DB Stop Service", "Trip Table Created");
    }

    public Cursor getAllStops(SQLiteDatabase db){
        String[] projections = {
                OctDbStopTableData.STOP_ID,
                OctDbStopTableData.STOP_CODE,
                OctDbStopTableData.STOP_NAME
        };
        Cursor cursor = db.query(
                OctDbStopTableData.TABLE_NAME,
                projections,
                null,null,null,null, null);
        Log.d("Oct DB Stop Service", "Entries Retrieved");

        return cursor;
    }

    public Cursor getStop(SQLiteDatabase db, String stopNum){
        Cursor cursor = db.rawQuery(
                "select * from "
                        + OctDbStopTableData.TABLE_NAME
                        + " where "
                        + OctDbStopTableData.STOP_CODE
                        + "=?",
                new String[] { stopNum });
        return cursor;
    }

    public void importFromCSV(SQLiteDatabase db){
        String mCSVfile = "stops.csv";
        AssetManager manager = this.context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.execSQL("DROP TABLE IF EXISTS '" + OctDbStopTableData.TABLE_NAME + "'");
        db.execSQL(createQuery);
        db.beginTransaction();
        boolean firstRowFlag = false;
        try {
            while ((line = buffer.readLine()) != null) {
                if (firstRowFlag != false) {
                    String[] colums = line.split(",", -1);
                    if (colums.length != 9) {
                        Log.d("CSVParser", "Skipping Bad CSV Row");
                        continue;
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(OctDbStopTableData.STOP_ID, colums[0].trim());
                    cv.put(OctDbStopTableData.STOP_CODE, colums[1].trim());
                    cv.put(OctDbStopTableData.STOP_NAME, colums[2].trim());
                    cv.put(OctDbStopTableData.STOP_DESC, colums[3].trim());
                    cv.put(OctDbStopTableData.STOP_LAT, colums[4].trim());
                    cv.put(OctDbStopTableData.STOP_LON, colums[5].trim());
                    cv.put(OctDbStopTableData.ZONE_ID, colums[6].trim());
                    cv.put(OctDbStopTableData.STOP_URL, colums[7].trim());
                    cv.put(OctDbStopTableData.LOCATION_TYPE, colums[8].trim());
                    db.insert(OctDbStopTableData.TABLE_NAME, null, cv);
                } else
                    firstRowFlag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        try {
            this.writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //for Debug purposes
    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            String currentDBPath = OctDbStopTableData.DATABASE_NAME;
            String backupDBPath = "ocDBBackup.db";
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
