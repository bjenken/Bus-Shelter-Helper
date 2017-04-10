package com.example.brettjenken.honourstutorial.Ui.Times;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.brettjenken.honourstutorial.R;

/**
 * Created by Brett on 11/24/2016.
 */
public class TimeAdapter extends ArrayAdapter{
    List list = new ArrayList();
    public TimeAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(TimeUIModel time) {
        list.add(time);
        super.add(time);
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
        TimeHolder timeHolder;
        if (row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.display_time_row_layout, parent, false);
            timeHolder = new TimeHolder();
            timeHolder.txTime = (TextView)row.findViewById(R.id.displayTimeRowLayoutTextViewTime);
            timeHolder.txDest = (TextView)row.findViewById(R.id.displayTimeRowLayoutTextViewDestination);
            row.setTag(timeHolder);
        }else {
            timeHolder = (TimeHolder)row.getTag();
        }
        TimeUIModel time = (TimeUIModel)getItem(position);
        timeHolder.txTime.setText(time.getTime());
        timeHolder.txDest.setText(time.getDestination());

        return row;
    }

    static class TimeHolder{
        TextView txTime, txDest;
    }
}
