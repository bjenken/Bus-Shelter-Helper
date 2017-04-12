package com.example.brettjenken.honourstutorial.Ui.Route;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Service.ServiceCallback;
import com.example.brettjenken.honourstutorial.ServiceModel.ServiceStopModel;
import com.example.brettjenken.honourstutorial.Ui.Times.TimesListActivity;
import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;
import com.example.brettjenken.honourstutorial.Ui.UiUtils;

public class RoutesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, UiBackgroundTaskCallback, ServiceCallback {
    ProgressDialog dialog;
    String stopId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_list);
        ListView lv = (ListView) findViewById(R.id.routesListListView);
        lv.setOnItemClickListener(RoutesListActivity.this);
        stopId = getIntent().getStringExtra("EXTRA_STOP_ID");
        //check db for stop
        RoutesBackgroundTask routesBackgroundTask = new RoutesBackgroundTask(this, this, this, null);
        dialog = ProgressDialog.show(RoutesListActivity.this, "",
                "Loading. Please wait...", true);
        routesBackgroundTask.execute(UiUtils.RouteBackgroundTaskInputValues.CHECK_APP_DB_FOR_STOP.name(), stopId);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Boolean multiRoute = false;
        RouteUiModel entry = (RouteUiModel) parent.getAdapter().getItem(position);
        //Here I am checking to see if the stop has multiple directions for this route
        //In that case, the api response is different so I will have to account for that
        for (int i = 0; i < parent.getAdapter().getCount(); i++){
            if (i != position) {
                RouteUiModel curr = (RouteUiModel) parent.getAdapter().getItem(i);
                if (curr.getNumber().equals(entry.getNumber())){
                    multiRoute = true;
                }
            }
        }
        Intent intent = new Intent(RoutesListActivity.this, TimesListActivity.class);
        intent.putExtra("EXTRA_ROUTE_NUM", entry.getNumber());
        intent.putExtra("EXTRA_STOP_ID", stopId);
        intent.putExtra("EXTRA_ROUTE_DIR", entry.getDirection());
        intent.putExtra("EXTRA_MULTI_ROUTE_FLAG", multiRoute);
        startActivity(intent);
    }


    @Override
    public void backGroundTaskSuccess(String result) {
        UiUtils.RouteBackgroundTaskReturnValues returnCase =
                UiUtils.RouteBackgroundTaskReturnValues.valueOf(result);
        switch(returnCase){
            case EMPTY:
                if (dialog.isShowing())
                    dialog.cancel();
                this.createDialog();
                return;
            case ROUTES_RETRIEVED_FROM_APP_DB:
                if (dialog.isShowing())
                    dialog.cancel();
                return;
            case ROUTES_RETRIEVED_FROM_STOP_OBJECT:
                if (dialog.isShowing())
                    dialog.cancel();
                return;
            case STOP_FOUND:
                RoutesBackgroundTask updateStop = new RoutesBackgroundTask(this, this, this, null);
                updateStop.execute(UiUtils.RouteBackgroundTaskInputValues.UPDATE_STOP_IN_APP_DB.name(), stopId);
                RoutesBackgroundTask routesBackgroundTask = new RoutesBackgroundTask(this, this, this, null);
                routesBackgroundTask.execute(UiUtils.RouteBackgroundTaskInputValues.GET_ROUTES_FROM_APP_DB.name(), stopId);
                break;
            case STOP_NOT_FOUND:
                RoutesBackgroundTask getStop = new RoutesBackgroundTask(this, this, this, null);
                getStop.execute(UiUtils.RouteBackgroundTaskInputValues.GET_STOP_FROM_OC_DB.name(), stopId);
                break;
        }
    }

    @Override
    public void backGroundTaskFailure(Exception e) {
        dialog.cancel();
    }

    public boolean createDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Routes For This Stop")
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
    public void serviceSuccess(ServiceStopModel stop) {
        RoutesBackgroundTask addStopBackgroundTask = new RoutesBackgroundTask(this, this, this, stop);
        addStopBackgroundTask.execute(UiUtils.RouteBackgroundTaskInputValues.ADD_STOP_TO_APP_DB.name());
        RoutesBackgroundTask routesBackgroundTask = new RoutesBackgroundTask(this, this, this, stop);
        routesBackgroundTask.execute(UiUtils.RouteBackgroundTaskInputValues.GET_ROUTES_FROM_STOP_OBJECT.name());
    }

    @Override
    public void serviceFailure(Exception e) {
        dialog.cancel();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
