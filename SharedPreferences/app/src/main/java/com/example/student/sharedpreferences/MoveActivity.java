package com.example.student.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MoveActivity extends AppCompatActivity {

    TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        tv_show = (TextView)findViewById(R.id.tv_show);

        SharedPreferences sharedPref = getSharedPreferences("filename", Context.MODE_PRIVATE);

        String data_str     = sharedPref.getString("key1", "no data");
        int data_int        = sharedPref.getInt("key2", -1);
        boolean data_bool   = sharedPref.getBoolean("key3", false);
        long data_long       = sharedPref.getLong("key4", -1);
        float data_float    = sharedPref.getFloat("key5", -1);
        Set<String> data_set = sharedPref.getStringSet("key6", new HashSet<String>());

        String text = "data_str : " + data_str + "\n" +
                "data_int : " + data_int + "\n" +
                "data_bool : " + data_bool + "\n" +
                "data_long : " + data_long + "\n" +
                "data_float : " + data_float + "\n" +
                "data_set : " + data_set + "\n";

        tv_show.setText(text);
    }
}