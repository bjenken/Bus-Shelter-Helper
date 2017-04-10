package com.example.brettjenken.honourstutorial.Ui.Stop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.brettjenken.honourstutorial.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Brett on 11/24/2016.
 */
public class StopAdapter extends ArrayAdapter{
    List list = new ArrayList();
    public StopAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(StopUiModel stop) {
        list.add(stop);
        super.add(stop);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StopHolder stopHolder;
        if (row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.display_stop_row_layout, parent, false);
            stopHolder = new StopHolder();
            stopHolder.txId = (TextView)row.findViewById(R.id.displayStopRowLayoutTextViewStopId);
            stopHolder.txLocation = (TextView)row.findViewById(R.id.displayStopRowLayoutTextViewLocation);
            stopHolder.txRoutes = (TextView)row.findViewById(R.id.displayStopRowLayoutTextViewRoutes);
            row.setTag(stopHolder);
        }else {
            stopHolder = (StopHolder)row.getTag();
        }
        StopUiModel stop = (StopUiModel)getItem(position);
        stopHolder.txId.setText(stop.getId().toString());
        stopHolder.txLocation.setText(stop.getLocation().toString());
        stopHolder.txRoutes.setText(stop.getRoutes().toString());

        return row;
    }

    static class StopHolder{
        TextView txId, txLocation, txRoutes;
    }
}
