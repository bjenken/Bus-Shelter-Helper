package com.example.brettjenken.honourstutorial.Ui.Stop;

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
import android.widget.Toast;

import com.example.brettjenken.honourstutorial.R;
import com.example.brettjenken.honourstutorial.Ui.Route.RoutesListActivity;
import com.example.brettjenken.honourstutorial.Ui.UiBackgroundTaskCallback;
import com.example.brettjenken.honourstutorial.Ui.UiUtils;

public class StopsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, UiBackgroundTaskCallback, DialogInterface.OnClickListener {
    String queryStopCode;
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
        stopsBackgroundTask.execute(UiUtils.StopBackGroundTaskInputValues.GET_ALL_STOPS.name());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StopUiModel entry= (StopUiModel) parent.getAdapter().getItem(position);
        Intent intent = new Intent(StopsListActivity.this, RoutesListActivity.class);
        intent.putExtra("EXTRA_STOP_ID", entry.getId());
        startActivity(intent);
    }

    public boolean createSearchDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setId(R.id.edit_text_stop_num);
        builder.setTitle("Enter StopUiModel ID")
                .setView(input)
                .setPositiveButton("View Stop", this)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
        return true;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Dialog d = (Dialog)dialog;
        EditText input = (EditText)d.findViewById(R.id.edit_text_stop_num);
        this.queryStopCode = input.getText().toString();
        //validate it here
        StopsBackgroundTask stopsBackgroundTask = new StopsBackgroundTask(this, this);
        stopsBackgroundTask.execute(UiUtils.StopBackGroundTaskInputValues.CHECK_FOR_STOP.name(), queryStopCode);
        //then throw the user over to the appropriate listing
    }
    @Override
    public void onBackPressed() {
        //avoids the user being sent to the blank MainActivity
    }

    @Override
    public void backGroundTaskSuccess(String result) {
        UiUtils.StopBackGroundTaskReturnValues returnCase =
                UiUtils.StopBackGroundTaskReturnValues.valueOf(result);
        switch(returnCase){
            case STOP_FOUND:
                Intent intent = new Intent(StopsListActivity.this, RoutesListActivity.class);
                intent.putExtra("EXTRA_STOP_ID", queryStopCode);
                startActivity(intent);
                return;
            case STOPS_RETRIEVED:
                return;
        }
    }

    @Override
    public void backGroundTaskFailure(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        StopsBackgroundTask stopsBackgroundTask = new StopsBackgroundTask(this, this);
        stopsBackgroundTask.execute(UiUtils.StopBackGroundTaskInputValues.GET_ALL_STOPS.name());
    }


}
