package com.example.student.sharedpreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button btn_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_move = (Button)findViewById(R.id.btn_move);
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MoveActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPref = getSharedPreferences("filename", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("key1", "this is string data");
        editor.putInt("key2", 1234);
        editor.putBoolean("key3", true);
        editor.putLong("key4", 1234);
        editor.putFloat("key5", 3.14f);

        Set<String> arr = new HashSet<String>();
        arr.add("hi");
        arr.add("android");
        editor.putStringSet("key6", arr);
        editor.commit();
    }
}