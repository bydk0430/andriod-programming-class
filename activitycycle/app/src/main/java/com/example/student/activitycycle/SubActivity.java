package com.example.student.activitycycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
    }

    protected void onStart() {
        super.onStart();
        Log.d("activity_cycle", "SubActivity에서 onStart() 호출");
    }

    protected void onResume() {
        super.onResume();
        Log.d("activity_cycle", "SubActivity에서 onResume() 호출");
    }

    protected void onPause() {
        super.onPause();
        Log.d("activity_cycle", "SubActivity에서 onPause() 호출");
    }

    protected void onStop() {
        super.onStop();
        Log.d("activity_cycle", "SubActivity에서 onStop() 호출");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("activity_cycle", "SubActivity에서 onDestroy() 호출");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d("activity_cycle", "SubActivity에서 onRestart() 호출");
    }
}
