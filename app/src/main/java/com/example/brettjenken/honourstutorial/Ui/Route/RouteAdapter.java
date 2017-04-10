package com.example.brettjenken.honourstutorial.Ui.Route;

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
public class RouteAdapter extends ArrayAdapter{
    List list = new ArrayList();
    public RouteAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(RouteUiModel route) {
        list.add(route);
        super.add(route);
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
        RouteHolder routeHolder;
        if (row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.display_route_row_layout, parent, false);
            routeHolder = new RouteHolder();
            routeHolder.txNum = (TextView)row.findViewById(R.id.displayRouteRowLayoutTextViewRouteNumber);
            routeHolder.txDirection = (TextView)row.findViewById(R.id.displayRouteRowLayoutTextViewDirection);
            row.setTag(routeHolder);
        }else {
            routeHolder = (RouteHolder)row.getTag();
        }
        RouteUiModel route = (RouteUiModel)getItem(position);
        routeHolder.txNum.setText(route.getNumber());
        routeHolder.txDirection.setText(route.getDirection());

        return row;
    }

    static class RouteHolder{
        TextView txNum, txDirection;
    }
}
