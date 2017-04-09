package com.example.brettjenken.honourstutorial.Route;

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
import com.example.brettjenken.honourstutorial.UIBackgroundTaskCallback;

public class RoutesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, UIBackgroundTaskCallback {
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
        RoutesBackgroundTask routesBackgroundTask = new RoutesBackgroundTask(this, this);
        dialog = ProgressDialog.show(RoutesListActivity.this, "",
                "Loading. Please wait...", true);
        routesBackgroundTask.execute("get_all_routes");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Boolean multiRoute = false;
        RouteUIModel entry = (RouteUIModel) parent.getAdapter().getItem(position);
        //Here I am checking to see if the stop has multiple directions for this route
        //In that case, the api response is different so I will have to account for that
        for (int i = 0; i < parent.getAdapter().getCount(); i++){
            if (i != position) {
                RouteUIModel curr = (RouteUIModel) parent.getAdapter().getItem(i);
                if (curr.getNumber().equals(entry.getNumber())){
                    multiRoute = true;
                }
            }
        }
        //Intent intent = new Intent(RoutesListActivity.this, TimesListActivity.class);
        //intent.putExtra("EXTRA_ROUTE_NUM", entry.getNumber());
        //intent.putExtra("EXTRA_STOP_ID", stopId);
        //intent.putExtra("EXTRA_ROUTE_DIR", entry.getDirection());
        //intent.putExtra("EXTRA_MULTI_ROUTE_FLAG", multiRoute);
        //startActivity(intent);
    }

    //@Override
    //public void serviceSuccess(StopAPIModel stop) {
    //    dialog.cancel();
    //    RoutesBackgroundTask addStopBackgroundTask = new RoutesBackgroundTask(this, this, stop);
    //    addStopBackgroundTask.execute("add_stop_entry");
    //    RoutesBackgroundTask routesBackgroundTask = new RoutesBackgroundTask(this, this, stop);
    //    routesBackgroundTask.execute("get_all_routes");
    //}

    //@Override
    //public void serviceFailure(Exception e) {
    //    dialog.cancel();
    //    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    //}

    @Override
    public void backGroundTaskSuccess(String result) {
        if (result.equals("Empty")){
            if (dialog.isShowing())
                dialog.cancel();
            this.createDialog();
        } else{
            if (dialog.isShowing())
                dialog.cancel();
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

}
