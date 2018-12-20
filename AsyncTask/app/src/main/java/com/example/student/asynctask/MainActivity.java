package com.example.student.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView_main;
    ProgressBar progressBar_main;
    Button button_main, button_end;
    MyTask myTask = null;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_main = (TextView)findViewById(R.id.textView_main);
        progressBar_main = (ProgressBar)findViewById(R.id.progressBar_main);
        button_main = (Button)findViewById(R.id.button_main);
        button_end = (Button)findViewById(R.id.button_end);

        button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myTask == null) {
                    myTask = new MyTask();
                    myTask.execute();
                } else {
                    Toast.makeText(MainActivity.this, "이미 동작하고 있습니다",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myTask != null) {
                    myTask.cancel(true);
                    myTask = null;
                }
            }
        });
    }

    class MyTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... argu) {
            while (isCancelled() == false) {
                value++;
                if(value <= 1000) {
                    publishProgress();
                } else {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }
            return null;
        }
        protected void onPreExecute() {
            value = 0;
            progressBar_main.setVisibility(View.INVISIBLE);
        }
        protected void onPostExecute(Void result) {
            textView_main.setText("1000번을 카운트 하였습니다");
            progressBar_main.setVisibility(View.INVISIBLE);
        }

        protected void onProgressUpdate(Void... argu) {
            progressBar_main.setVisibility(View.VISIBLE);
            textView_main.setText(Integer.toString(value));
        }

        protected void onCancelled() {
            textView_main.setText("사용자에 의해 종료되었습니다");
            progressBar_main.setVisibility(View.INVISIBLE);
        }
    }
}
