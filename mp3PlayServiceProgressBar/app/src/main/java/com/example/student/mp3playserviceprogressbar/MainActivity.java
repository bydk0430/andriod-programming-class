package com.example.student.mp3playserviceprogressbar;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean bReadPerm = false;          //SD카드를 읽기 위한 여부를 저장
    Button button_play, button_stop;    // 버튼 객체를 위한 변수
    boolean bStatePlay = false;         // 재생 상태 유무를 위한 변수,
    // true : 재생, false : 정지
    MyAsyncTask myAsyncTask = null;
    ProgressBar pb_running;

    TextView tv_curtime, tv_fulltime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // sd 카드 읽기를 위한 권한을 처리하는 함수
        setPermission();

        pb_running = (ProgressBar) findViewById(R.id.pb_running);
        tv_curtime = (TextView) findViewById(R.id.tv_curtime);
        tv_fulltime = (TextView)findViewById(R.id.tv_fulltime);

        // 버튼 객체를 생성
        button_play = (Button) findViewById(R.id.button_play);
        button_stop = (Button) findViewById(R.id.button_stop);

        // 버튼의 리스너 등록
        button_play.setOnClickListener(new myButtonListener());
        button_stop.setOnClickListener(new myButtonListener());

        // 서비스와 통신을 위해 리시버를 등록
        registerReceiver(receiver, new IntentFilter("com.example.student.mp3playserviceprogressbar"));

        if(bReadPerm) {
            // SD 카드의 상태를 확인
            String state = Environment.getExternalStorageState();

            if (state.equals(Environment.MEDIA_MOUNTED)) {
                // SD카드가 장착이 되어 있다면
                try {
                    // SD 카드 안에 있는 mp3 파일의 경로를 읽어온다.
                    String musicPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/music.mp3";
                    // 인텐트에 mp3 파일 경로를 저장한다.
                    Intent intent = new Intent(MainActivity.this, PlayService.class);
                    intent.putExtra("filePath", musicPath);
                    // 서비스를 시작한다.
                    startService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String transMillsec(int mill) {
        String result = "";

        int sec = (mill / 1000) % 60;
        int min = (mill / (1000 * 60)) % 60;
        int hour = (mill / (1000 * 60 * 60)) % 24;

        if(hour < 10) {
            result += "0"+hour+":";
        } else {
            result += hour+":";
        }

        if(min < 10) {
            result += "0"+min+":";
        } else {
            result += hour+":";
        }

        if(sec < 10) {
            result += "0"+sec;
        } else {
            result += sec;
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MyPlayerService_log", "Main onDestroy()");

        // 엑티비티가 사라질 때 리시버도 해제
        unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getStringExtra("state");
            if(state != null) {
                if (state.equals("play")) {
                    bStatePlay = true;
                    if(myAsyncTask == null) {
                        myAsyncTask = new MyAsyncTask();
                        myAsyncTask.execute();
                    }
                    button_play.setText("Pause");
                } else if(state.equals("pause") || state.equals("stop")) {

                    bStatePlay = false;
                    if(myAsyncTask != null) {
                        myAsyncTask.cancel(true);
                        myAsyncTask = null;
                    }
                    if(state.equals("stop")) {
                        pb_running.setProgress(0);
                        //tv_fulltime.setText(transMillsec(Integer.valueOf(0)));
                        tv_curtime.setText(transMillsec(Integer.valueOf(0)));
                    }

                    button_play.setText("Play");
                }
            }

            String cur_time = intent.getStringExtra("cur_time");
            if(cur_time != null) {
                pb_running.setProgress(Integer.valueOf(cur_time));

                tv_curtime.setText(transMillsec(Integer.valueOf(cur_time)));

                Log.d("pb_running_test", "cur : " + cur_time);
            }

            String full_time = intent.getStringExtra("full_time");
            if(full_time != null) {
                pb_running.setMax(Integer.valueOf(full_time));

                tv_fulltime.setText(transMillsec(Integer.valueOf(full_time)));
                Log.d("pb_running_test", "full_time : " + full_time);
            }
        }
    };

    class myButtonListener implements View.OnClickListener {
        Intent intent;

        @Override
        public void onClick(View view) {
            intent = new Intent("com.example.student.mp3playserviceprogressbar");
            switch(view.getId()) {
                case R.id.button_play:
                    if(bStatePlay) {
                        intent.putExtra("btn", "pause");
                    } else {
                        intent.putExtra("btn", "play");
                    }
                    break;
                case R.id.button_stop:
                    intent.putExtra("btn", "stop");
                    break;
            }
            sendBroadcast(intent);
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        Intent intent = new Intent("com.example.student.mp3playserviceprogressbar");

        @Override
        protected void onPreExecute() {
            intent.putExtra("time", "running_time");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while(isCancelled() == false) {
                SystemClock.sleep(500);
                sendBroadcast(intent);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void setPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            bReadPerm = true;
        }


        if(!bReadPerm) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 200);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bReadPerm = true;
            }
        }
    }
}
