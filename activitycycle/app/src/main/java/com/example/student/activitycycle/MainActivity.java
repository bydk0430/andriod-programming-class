package com.example.student.activitycycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_main = (Button)findViewById(R.id.button_main);
        button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        SubActivity.class);
                startActivity(intent);
            }
        });
        Log.d("activity_cycle", "MainActivity에서 onCreate() 호출");
    }

    protected void onStart() {
        super.onStart();
        Log.d("activity_cycle", "MainActivity에서 onStart() 호출");
    }

    protected void onResume() {
        super.onResume();
        Log.d("activity_cycle", "MainActivity에서 onResume() 호출");
    }

    protected void onPause() {
        super.onPause();
        Log.d("activity_cycle", "MainActivity에서 onPause() 호출");
    }

    protected void onStop() {
        super.onStop();
        Log.d("activity_cycle", "MainActivity에서 onStop() 호출");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("activity_cycle", "MainActivity에서 onDestroy() 호출");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d("activity_cycle", "MainActivity에서 onRestart() 호출");
    }
}
