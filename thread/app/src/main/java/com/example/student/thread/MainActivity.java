package com.example.student.thread;

import android.os.Handler;
import android.os.Message;
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

    MyThread2 myThread2 = null;

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
     * Thread 클래스 활용
     */

    class MyThread2 extends Thread {

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if(value<1000) {
                        Thread.sleep(1000);
                        value++;
                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = value;
                        my_handler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Handler my_handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                if(msg.arg1 < 1000) {
                    tv_count.setText(Integer.toString(msg.arg1));
                    pb_circle.setVisibility(View.VISIBLE);
                } else  {
                    tv_count.setText("1000번을 카운트 하였습니다");
                    pb_circle.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    class BtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
                    if(myThread2 == null) {
                        myThread2 = new MyThread2();
                        myThread2.start();
                    }
                    break;
                case R.id.btn_stop:
                    if(myThread2 != null) {
                        myThread2.interrupt();
                        tv_count.setText("사용자에 의해 종료되었습니다");
                        pb_circle.setVisibility(View.INVISIBLE);
                        myThread2 = null;
                    }
                    break;
            }
        }
    }

}
