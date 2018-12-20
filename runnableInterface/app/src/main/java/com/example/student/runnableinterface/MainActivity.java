package com.example.student.runnableinterface;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv_count;
    Button btn_start, btn_stop;
    ProgressBar pb_circle;
    int value = 0;
    Thread my_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_count = (TextView)findViewById(R.id.tv_count);
        btn_stop = (Button)findViewById(R.id.btn_stop);
        btn_start = (Button)findViewById(R.id.btn_start);
        pb_circle = (ProgressBar)findViewById(R.id.pb_circle);

        btn_stop.setOnClickListener(new BtnListener());
        btn_start.setOnClickListener(new BtnListener());

    }

    /**
     * Runnable 인터페이스 활용
     */
    //*
    Handler handler = new Handler();

    class UIUpdate implements Runnable {
        @Override
        public void run() {
            if(value < 1000) {
                tv_count.setText(Integer.toString(value));
                pb_circle.setVisibility(View.VISIBLE);
            } else {
                tv_count.setText("1000번을 카운트 하였습니다.");
                pb_circle.setVisibility(View.INVISIBLE);
            }

        }
    }

    class MyThread implements Runnable {

        @Override
        public void run() {

            try{
                while(!Thread.currentThread().isInterrupted()) {
                    if(value < 1000) {
                        SystemClock.sleep(1000);
                        value++;
                        handler.post(new UIUpdate());
                    }

                }
            } catch(Exception e) {
                e.printStackTrace();
            }


        }
    }

    class BtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_start:
                    if(my_thread == null) {
                        MyThread runnable = new MyThread();
                        my_thread = new Thread(runnable);
                        my_thread.start();
                    }
                    break;
                case R.id.btn_stop:
                    if(my_thread != null) {
                        my_thread.interrupt();
                        tv_count.setText("사용자에 의해 종료되었습니다.");
                        pb_circle.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    }
}
