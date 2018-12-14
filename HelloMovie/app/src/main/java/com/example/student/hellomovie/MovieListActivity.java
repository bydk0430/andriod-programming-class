package com.example.student.hellomovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.student.hellomovie.form.ListViewItem;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {

    ListView lv_movielist;
    ArrayAdapter adapter;

    final String[] list = {
            "블랙팬서", "궁합", "리틀포레스트", "월요일이 사라졌다" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //  2.리스트뷰 객체 만들기
        lv_movielist = (ListView)findViewById(R.id.lv_movielist);

        //  Adapter 에게 전달할 데이터 구성하기
        ListViewItem item = new ListViewItem("블랙팬서","2018.02", R.drawable.black);
        ArrayList<ListViewItem> arrayList = new ArrayList<ListViewItem>();
        arrayList.add(item);
        arrayList.add(
                new ListViewItem("궁합","2018.02", R.drawable.matching));
        arrayList.add(
                new ListViewItem("리틀포레스트","2018.02", R.drawable.little));
        arrayList.add(
                new ListViewItem("월요일이 사라졌다","2018.02", R.drawable.mon));


        //  3.리스트뷰에 Adapter 등록하기
        /**
         * 첫번째 매개변수 : 액티비티 정보(context 객체)
         * 두번째 매개변수 : 리스트뷰 항목의 레이아웃(안드로이드 제공)
         * 세번째 매개변수 : 표시할 데이터들
         */
        adapter = new ArrayAdapter<String>(MovieListActivity.this,
                android.R.layout.simple_list_item_1,list);

        // 3-1. 새로 만든 어답터를 등록한다.
//        ListViewAdapter listViewAdapter = new ListViewAdapter(
//                MovieListActivity.this, R.layout.listview_item, arrayList);


        ListViewAdapter2 listViewAdapter2 = new ListViewAdapter2(
                MovieListActivity.this, R.layout.listview_item2, arrayList);


        lv_movielist.setAdapter(listViewAdapter2);

        //  4.리스트뷰에 onItemClickListener 등록하기
        lv_movielist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Toast.makeText(MovieListActivity.this, i + " 선택함",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
