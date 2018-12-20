package com.example.student.progressbar;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb_circle, pb_bar;
    Button btn_start, btn_stop;
    MyAsyncTask myAsyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_circle = (ProgressBar)findViewById(R.id.pb_circle);
        pb_bar = (ProgressBar)findViewById(R.id.pb_bar);
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_stop = (Button)findViewById(R.id.btn_stop);

        pb_circle.setVisibility(View.VISIBLE);

        btn_stop.setOnClickListener(new myBtnListener());
        btn_start.setOnClickListener(new myBtnListener());

    }



    private class myBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_start:
                    if(myAsyncTask == null) {
                        myAsyncTask = new MyAsyncTask();
                        myAsyncTask.execute();
                    }
                    break;
                case R.id.btn_stop:
                    if(myAsyncTask != null) {
                        myAsyncTask.cancel(true);
                        myAsyncTask = null;
                    }
                    break;
            }
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        int value;

        @Override
        protected Void doInBackground(Void... voids) {
            while (isCancelled() == false) {
                value++;

                SystemClock.sleep(10);

                if(value <= 100) {
                    publishProgress(value);
                } else {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            value = 0;
            pb_bar.setMax(100);
            pb_bar.setProgress(0);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this, "100%완료 됨",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pb_bar.setProgress(values[0]);
            Log.d("my_onProgressUpdate", values[0].toString());
        }

        @Override
        protected void onCancelled() {
            pb_bar.setProgress(0);
        }
    }
}
