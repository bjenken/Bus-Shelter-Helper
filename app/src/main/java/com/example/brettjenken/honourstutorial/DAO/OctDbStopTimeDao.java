package com.example.brettjenken.honourstutorial.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbStopTimeTableData;
import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbTripTableData;

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

public class OctDbStopTimeDao extends SQLiteOpenHelper {
    public static final int databaseVersion = 1;
    private Context context;
    public String createQuery = "CREATE TABLE " + OctDbStopTimeTableData.TABLE_NAME
            + "("
            + OctDbStopTimeTableData.TRIP_ID + " TEXT, "
            + OctDbStopTimeTableData.ARRIVAL_TIME + " TEXT, "
            + OctDbStopTimeTableData.DEPARTURE_TIME + " TEXT, "
            + OctDbStopTimeTableData.STOP_ID + " TEXT, "
            + OctDbStopTimeTableData.STOP_SEQUENCE + " TEXT, "
            + OctDbStopTimeTableData.PICKUP_TYPE + " TEXT, "
            + OctDbStopTimeTableData.DROP_OFF_TYPE + " TEXT"
            + ");";

    public OctDbStopTimeDao(Context context) {
        super(context, OctDbStopTimeTableData.DATABASE_NAME, null, databaseVersion);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createQuery);
        this.importFromCSV(db);
        Log.d("OC DB Stop Time Service", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(createQuery);
        Log.d("OC DB Stop Time Service", "Table Created");
    }

    public Cursor getUniqueTripsForStop(SQLiteDatabase db, List<String> stopIds){ //this is not the id the user inputs
        String serviceId = "%JANDA17%";
        String[] input = stopIds.toArray(new String[0]);
        Cursor cursor = db.rawQuery(
                "select *"
                        + " from "
                        + OctDbStopTimeTableData.TABLE_NAME
                        + " natural join "
                        + OctDbTripTableData.TABLE_NAME
                        + " where "
                        + OctDbStopTimeTableData.STOP_ID
                        + " in ("
                        + this.makePlaceholders(input.length)
                        + ")"
                        + " and "
                        + OctDbTripTableData.SERVICE_ID
                        + " like '"
                        + serviceId
                        + "' group by "
                        + OctDbTripTableData.ROUTE_ID
                        + " , "
                        + OctDbTripTableData.DIRECTION_ID,
                input);
        return cursor;
    }

    public void importFromCSV(SQLiteDatabase db){
        String mCSVfile = "stop_times.csv";
        AssetManager manager = this.context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.execSQL("DROP TABLE IF EXISTS '" + OctDbStopTimeTableData.TABLE_NAME + "'");
        db.execSQL(createQuery);
        db.beginTransaction();
        boolean firstRowFlag = false;
        try {
            while ((line = buffer.readLine()) != null) {
                if (firstRowFlag != false) {
                    String[] colums = line.split(",", -1);
                    if (colums.length != 7) {
                        Log.d("CSVParser", "Skipping Bad CSV Row");
                        continue;
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(OctDbStopTimeTableData.TRIP_ID, colums[0].trim());
                    cv.put(OctDbStopTimeTableData.ARRIVAL_TIME, colums[1].trim());
                    cv.put(OctDbStopTimeTableData.DEPARTURE_TIME, colums[2].trim());
                    cv.put(OctDbStopTimeTableData.STOP_ID, colums[3].trim());
                    cv.put(OctDbStopTimeTableData.STOP_SEQUENCE, colums[4].trim());
                    cv.put(OctDbStopTimeTableData.PICKUP_TYPE, colums[5].trim());
                    cv.put(OctDbStopTimeTableData.DROP_OFF_TYPE, colums[6].trim());
                    db.insert(OctDbStopTimeTableData.TABLE_NAME, null, cv);
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
            String currentDBPath = OctDbStopTimeTableData.DATABASE_NAME;
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

    private String makePlaceholders(int length) {
        if (length < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(length * 2 - 1);
            sb.append("?");
            for (int i = 1; i < length; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}
