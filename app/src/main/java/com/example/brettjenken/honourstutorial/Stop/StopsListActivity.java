package com.example.brettjenken.honourstutorial.Stop;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Route.RoutesListActivity;
import com.example.brettjenken.honourstutorial.UIBackgroundTaskCallback;

public class StopsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, UIBackgroundTaskCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSearchDialog();
            }
        });
        ListView lv = (ListView) findViewById(R.id.stopsListListView);
        lv.setOnItemClickListener(StopsListActivity.this);

        StopsBackgroundTask stopsBackgroundTask = new StopsBackgroundTask(this, this);
        stopsBackgroundTask.execute("get_all_stops");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StopUIModel entry= (StopUIModel) parent.getAdapter().getItem(position);
        Intent intent = new Intent(StopsListActivity.this, RoutesListActivity.class);
        intent.putExtra("EXTRA_STOP_ID", entry.getId());
        startActivity(intent);
    }

    public boolean createSearchDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setId(R.id.edit_text_stop_num);
        builder.setTitle("Enter StopUIModel ID")
                .setView(input)
                .setPositiveButton("View StopUIModel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        Dialog d = (Dialog)dialog;
                        EditText input = (EditText)d.findViewById(R.id.edit_text_stop_num);
                        String query = input.getText().toString();
                        //validate it here
                        //then throw the user over to the appropriate listing
                        Intent intent = new Intent(StopsListActivity.this, RoutesListActivity.class);
                        intent.putExtra("EXTRA_STOP_ID", query);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
        return true;
    }

    @Override
    public void onBackPressed() {
        //avoids the user being sent to the blank MainActivity
    }

    @Override
    public void backGroundTaskSuccess(String result) {

    }

    @Override
    public void backGroundTaskFailure(Exception e) {

    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        StopsBackgroundTask stopsBackgroundTask = new StopsBackgroundTask(this, this);
        stopsBackgroundTask.execute("get_all_stops");
    }
}
