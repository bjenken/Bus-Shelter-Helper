package com.example.brettjenken.honourstutorial.Factory;

import android.database.Cursor;

import com.example.brettjenken.honourstutorial.OctDbModel.OctDbStopModel;
import com.example.brettjenken.honourstutorial.OctDbTableData.OctDbStopTableData;


/**
 * Created by Brett on 2/18/2017.
 */

public class OctDbStopModelFactory {
    public OctDbStopModelFactory(){
    }

    public OctDbStopModel getStopOctDbModel(){
        return new OctDbStopModel();
    }

    public OctDbStopModel getStopOctDbModel(Cursor cursor){
        cursor.moveToFirst();
        OctDbStopModel output = getStopOctDbModel();
        output.setStopId(cursor.getString(cursor.getColumnIndex(OctDbStopTableData.STOP_ID)).replace("\"", ""));
        output.setStopCode(cursor.getString(cursor.getColumnIndex(OctDbStopTableData.STOP_CODE)).replace("\"", ""));

        String name1 = cursor.getString(cursor.getColumnIndex(OctDbStopTableData.STOP_NAME)).replace("\"", "");
        String nameFinal;
        if (cursor.moveToNext()){
            String name2 = cursor.getString(cursor.getColumnIndex(OctDbStopTableData.STOP_NAME)).replace("\"", "");
            //In the case of a stopCode with multiple stops (i.e Billings Bridge), this will grab the
            //common titles I.e "Billings Bridge 4A" "Billings Bridge 2B" -> "Billings Bridge"
            nameFinal = this.longestCommonSubstring(name1, name2);
        }else
            nameFinal = name1;

        output.setStopName(nameFinal);
        return output;
    }

    private static String longestCommonSubstring(String s1, String s2)
    {
        int start = 0;
        int max = 0;
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                int x = 0;
                while (s1.charAt(i + x) == s2.charAt(j + x)) {
                    x++;
                    if (((i + x) >= s1.length()) || ((j + x) >= s2.length()))
                        break;
                }
                if (x > max) {
                    max = x;
                    start = i;
                }
            }
        }
        return s1.substring(start, (start + max));
    }

}
