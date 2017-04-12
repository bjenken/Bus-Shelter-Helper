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
import android.widget.Toast;

import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Service.ApiService;
import com.example.brettjenken.honourstutorial.Service.ServiceCallback;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceStopModel;
import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;
import com.example.brettjenken.honourstutorial.Ui.UiUtils;

public class TimesListActivity extends AppCompatActivity implements UiBackgroundTaskCallback, ServiceCallback {
    String stopId, routeNum, direction;
    Boolean multiRoute;
    Dialog dialog;
    ApiService apiService;
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

        apiService= new ApiService(TimesListActivity.this);
        apiService.execute(UiUtils.ApiServiceInputValues.GET_ALL_TIMES.name(), stopId, routeNum, direction, multiRoute.toString());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(TimesListActivity.this, "",
                        "Refreshing times. Please wait...", true);
                apiService= new ApiService(TimesListActivity.this);
                apiService.execute(UiUtils.ApiServiceInputValues.GET_ALL_TIMES.name(), stopId, routeNum, direction, multiRoute.toString());

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
        UiUtils.TimesBackgroundTaskReturnValues resultCase =
                UiUtils.TimesBackgroundTaskReturnValues.valueOf(result);
        switch (resultCase) {
            case EMPTY:
                if (dialog.isShowing())
                    dialog.cancel();
                this.createDialog();
                return;
            case TIMES_RETRIEVED:
                if (dialog.isShowing())
                    dialog.cancel();
                return;
        }
    }

    @Override
    public void backGroundTaskFailure(Exception e) {
        if (dialog.isShowing())
            dialog.cancel();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void serviceSuccess(ServiceStopModel stop) {
        if (dialog.isShowing())
            dialog.cancel();
        TimesBackgroundTask timesBackgroundTask = new TimesBackgroundTask(this, this, stop);
        timesBackgroundTask.execute(UiUtils.TimesBackgroundTaskInputValues.GET_ALL_TIMES.name());
    }

    @Override
    public void serviceFailure(Exception e) {
        if (dialog.isShowing())
            dialog.cancel();
        createDialog();
        e.printStackTrace();
    }
}
