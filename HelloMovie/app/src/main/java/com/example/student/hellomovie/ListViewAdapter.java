package com.example.student.hellomovie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.hellomovie.form.ListViewItem;

import java.util.ArrayList;

/**
 * Created by student on 2018-12-13.
 */

public class ListViewAdapter extends BaseAdapter {
    //  자료를 저장하고 있는 ArrayList
    ArrayList<ListViewItem> list;
    Context context;
    int item_layout;
    LayoutInflater layoutInflater;

    public ListViewAdapter(
            Context context,
            int item_layout,
            ArrayList<ListViewItem> list) {
        this.context = context;
        this.item_layout = item_layout;
        this.list = list;
        layoutInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        if(view == null) {
            view = layoutInflater.inflate(item_layout, viewGroup, false);
        }

        ImageView iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
        iv_thumb.setImageResource(list.get(pos).getImg_id());
        iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                       list.get(pos).getTitle()+"를(을) 선택하셨습니다" ,
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra("movie_index", pos);
                intent.putExtra("movie_title", list.get(pos).getTitle());
                intent.putExtra("movie_date", list.get(pos).getDate());
                intent.putExtra("movie_img_id", list.get(pos).getImg_id());
                context.startActivity(intent);
            }
        });

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(list.get(pos).getTitle());

        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_date.setText(list.get(pos).getDate());

        return view;
    }
}
