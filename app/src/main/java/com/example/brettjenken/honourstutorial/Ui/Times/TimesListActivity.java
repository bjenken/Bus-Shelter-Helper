package com.example.brettjenken.honourstutorial.Ui.Times;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;

public class TimesListActivity extends AppCompatActivity implements UiBackgroundTaskCallback {
    String stopId, routeNum, direction;
    Boolean multiRoute;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times_list);

        routeNum = getIntent().getStringExtra("EXTRA_ROUTE_NUM");
        stopId = getIntent().getStringExtra("EXTRA_STOP_ID");
        direction = getIntent().getStringExtra("EXTRA_ROUTE_DIR");
        multiRoute = getIntent().getExtras().getBoolean("EXTRA_MULTI_ROUTE_FLAG");



        dialog = ProgressDialog.show(TimesListActivity.this, "",
                "Loading. Please wait...", true);

        TimesBackgroundTask timesBackgroundTask = new TimesBackgroundTask(this, this);
        timesBackgroundTask.execute("get_all_times");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    public boolean createDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Current Trips For This Route")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        goBack();
                    }
                });
        builder.create().show();
        return true;
    }

    private void goBack(){
        super.onBackPressed();
    }

    @Override
    public void backGroundTaskSuccess(String result) {
        if (result.equals("Empty")){
            if (dialog.isShowing())
                dialog.cancel();
            this.createDialog();
        }
        else{
            if (dialog.isShowing())
                dialog.cancel();
        }
    }

    @Override
    public void backGroundTaskFailure(Exception e) {
            this.createDialog();
    }
}
