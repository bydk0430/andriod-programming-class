package com.example.student.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by student on 2018-12-26.
 */

public class DeviceAdapter extends BaseAdapter {
    ArrayList<BluetoothDevice> list;
    Context context;
    int layout_id;
    LayoutInflater layoutInflater;

    public DeviceAdapter(Context context, int layout_id,
                         ArrayList<BluetoothDevice> list) {
        this.context = context;
        this.layout_id = layout_id;
        this.list = list;
        this.layoutInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if(convertView == null) {
            convertView = layoutInflater.inflate(layout_id, parent, false);
        }

        TextView tv_name = (TextView)  convertView.findViewById(R.id.tv_name);
        TextView tv_addr = (TextView)  convertView.findViewById(R.id.tv_addr);

        tv_name.setText(list.get(pos).getName());
        tv_addr.setText(list.get(pos).getAddress());

        return convertView;
    }
}
