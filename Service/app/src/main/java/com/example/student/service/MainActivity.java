package com.example.student.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn_start, btn_stop, btn_send1, btn_send2;
    TextView tv_rev;
    BroadcastReceiver receiver_from_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService2.class);
                startService(intent);
            }
        });

        btn_stop = (Button)findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService2.class);
                stopService(intent);
            }
        });

        btn_send1 = (Button)findViewById(R.id.btn_send1);
        btn_send1.setOnClickListener(new myButtonListener());

        btn_send2 = (Button)findViewById(R.id.btn_send2);
        btn_send2.setOnClickListener(new myButtonListener());

        tv_rev = (TextView)findViewById(R.id.tv_rev);

        // activity의 receiver
        // 1. activity에서 intent를 생성하여 service의 receiver로 보냄
        // 2. service의 receiver가 받음
        // 3. service에서 intent를 생성하여 activity의 receiver로 보냄
        // 4. activity의 receiver가 받음
        // 5. 반복하면서 상호작용
        receiver_from_service = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String res = intent.getStringExtra("res");

                if(res != null) {
                    tv_rev.setText(res);
                }
            }
        };

        registerReceiver(receiver_from_service, new IntentFilter("com.example.student.service"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver_from_service);
    }

    class myButtonListener implements View.OnClickListener {

        Intent intent;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send1:
                    intent = new Intent("com.example.student.service");
                    intent.putExtra("mode","send1");
                    break;
                case R.id.btn_send2:
                    intent = new Intent("com.example.student.service");
                    intent.putExtra("mode","send2");
                    break;
            }
            sendBroadcast(intent);
        }
    }
}
